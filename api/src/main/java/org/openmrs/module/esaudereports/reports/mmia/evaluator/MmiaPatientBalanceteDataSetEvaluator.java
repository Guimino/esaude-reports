package org.openmrs.module.esaudereports.reports.mmia.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.esaudereports.reports.mmia.definition.MmiaPatientBalanceteDataSetDefinition;
import org.openmrs.module.esaudereports.reports.mmia.util.MmiaSqlSource;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.MapDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

@Handler(supports = MmiaPatientBalanceteDataSetDefinition.class)
public class MmiaPatientBalanceteDataSetEvaluator implements DataSetEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		MapDataSet dataSet = new MapDataSet(dataSetDefinition, evalContext);
		
		MmiaPatientBalanceteDataSetDefinition definition = (MmiaPatientBalanceteDataSetDefinition) dataSetDefinition;
		
		String startDate = DateUtil.formatDate(definition.getStartDate(), "yyyy-MM-dd");
		String endDate = DateUtil.formatDate(definition.getEndDate(), "yyyy-MM-dd");
		
		Long newEnrolledPatient = getUniqResult(evalContext, MmiaSqlSource.SQL_FIND_NEW_ENROLLED_PATIENT_BY_PERIOD,
		    startDate, endDate);
		
		Long changedPatientByPeriod = getUniqResult(evalContext, MmiaSqlSource.SQL_FIND_CHANGED_PATIENT_BY_PERIOD,
		    startDate, endDate);
		
		Long maintaningPatientInPeriod = getUniqResult(evalContext,
		    MmiaSqlSource.SQL_FIND_MAINTAINING_PATIENTS_BEFORE_PERIOD, startDate, endDate);
		
		DataSetRow row = new DataSetRow();
		row.addColumnValue(new DataSetColumn("NEW_PATIENTS", "NEW_PATIENTS", Long.class), newEnrolledPatient);
		row.addColumnValue(new DataSetColumn("CHANGED_PATIENTS", "CHANGED_PATIENTS", Long.class), changedPatientByPeriod);
		row.addColumnValue(new DataSetColumn("MAINTAINING_PATIENTS", "MAINTAINING_PATIENTS", Long.class),
		    maintaningPatientInPeriod);
		
		row.addColumnValue(new DataSetColumn("LOCATION_NAME", "LOCATION_NAME", String.class), Context.getLocationService()
		        .getDefaultLocation().getName());
		
		dataSet.addRow(row);
		
		return dataSet;
		
	}
	
	private Long getUniqResult(EvaluationContext evalContext, String sql, String startDate, String endDate) {
		
		String q = sql.replaceAll(":startDate", "'" + startDate + "'");
		q = q.replaceAll(":endDate", "'" + endDate + "'");
		
		SqlQueryBuilder query = new SqlQueryBuilder(q);
		
		return evaluationService.evaluateToObject(query, Long.class, evalContext);
	}
	
}
