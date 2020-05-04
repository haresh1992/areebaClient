package com.test.spring.activiti.integration.service;

import com.test.spring.activiti.integration.vo.User;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestService {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private HistoryService historyService;

	public String startProcess(final User user) throws ParseException {

		String result = null;

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test");

		while (processInstance != null && !processInstance.isEnded()) {

			List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();

			System.out.println("Active outstanding tasks: [" + tasks.size() + "]");

			for (int i = 0; i < tasks.size(); i++) {

				Task task = tasks.get(i);

				System.out.println("Processing Task [" + task.getName() + "]");

				Map<String, Object> variables = new HashMap<>();

				FormData formData = formService.getTaskFormData(task.getId());

				for (FormProperty formProperty : formData.getFormProperties()) {

					if (StringFormType.class.isInstance(formProperty.getType())
							&& formProperty.getId().equalsIgnoreCase("fullName"))
						variables.put(formProperty.getId(), user.getName());
					else if (StringFormType.class.isInstance(formProperty.getType())
							&& formProperty.getId().equalsIgnoreCase("role"))
						variables.put(formProperty.getId(), user.getRole());
					else if (DateFormType.class.isInstance(formProperty.getType()))
						variables.put(formProperty.getId(), user.getDate());
				}

				taskService.complete(task.getId(), variables);

				HistoricActivityInstance endActivity = null;

				List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
						.processInstanceId(processInstance.getId()).finished().orderByHistoricActivityInstanceEndTime()
						.asc().list();

				for (HistoricActivityInstance activity : activities) {

					if (activity.getActivityType().equals("startEvent"))
						System.out.println(
								"BEGIN [" + processInstance.getProcessDefinitionKey() + "] " + activity.getStartTime());

					if (activity.getActivityType().equals("endEvent"))
						endActivity = activity;

					else {
						System.out.println("-- " + activity.getActivityName() + " [" + activity.getActivityId() + "] "
								+ activity.getDurationInMillis() + " ms");

						result = activity.getActivityId();
					}
				}

				if (endActivity != null) {

					System.out.println("-- " + endActivity.getActivityName() + " [" + endActivity.getActivityId() + "] "
							+ endActivity.getDurationInMillis() + " ms");
					System.out.println(
							"COMPLETE [" + processInstance.getProcessDefinitionKey() + "] " + endActivity.getEndTime());
				}
			}

			processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId())
					.singleResult();
		}

		return result;
	}
}
