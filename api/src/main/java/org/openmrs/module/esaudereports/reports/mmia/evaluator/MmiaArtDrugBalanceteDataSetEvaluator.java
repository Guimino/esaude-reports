package org.openmrs.module.esaudereports.reports.mmia.evaluator;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.annotation.Handler;
import org.openmrs.module.esaudereports.reports.mmia.definition.MmiaArtDrugBalanceteDataSetDefinition;
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

@Handler(supports = MmiaArtDrugBalanceteDataSetDefinition.class)
public class MmiaArtDrugBalanceteDataSetEvaluator implements DataSetEvaluator {

	@Autowired
	private EvaluationService evaluationService;

	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext)
			throws EvaluationException {

		MapDataSet dataSet = new MapDataSet(dataSetDefinition, evalContext);
		MmiaArtDrugBalanceteDataSetDefinition definition = (MmiaArtDrugBalanceteDataSetDefinition) dataSetDefinition;

		String startDate = DateUtil.formatDate(definition.getStartDate(), "yyyy-MM-dd");
		String endDate = DateUtil.formatDate(definition.getEndDate(), "yyyy-MM-dd");

		String q = MmiaSqlSource.SQL_FIND_BALANCETE_BY_PERIOD.replaceAll(":startDate", "'" + startDate + "'");
		q = q.replaceAll(":endDate", "'" + endDate + "'");

		SqlQueryBuilder query = new SqlQueryBuilder(q);
		List<Object[]> results = evaluationService.evaluateToList(query, evalContext);

		if (results.isEmpty()) {
			setEmptyData(dataSet);
		}
		for (Object[] o : results) {

			DataSetRow row = new DataSetRow();
			row.addColumnValue(new DataSetColumn("FNM", "FNM", String.class), String.valueOf(o[0]));
			row.addColumnValue(new DataSetColumn("NAME", "NAME", String.class), String.valueOf(o[1]));
			row.addColumnValue(new DataSetColumn("OUTPUT_UNIT", "OUTPUT_UNIT", Integer.class), String.valueOf(o[2]));
			row.addColumnValue(new DataSetColumn("INITIAL_BALANCE", "INITIAL_BALANCE", Integer.class),
					String.valueOf(o[3]));
			row.addColumnValue(new DataSetColumn("ENTRANCES", "ENTRANCES", Integer.class), String.valueOf(o[4]));
			row.addColumnValue(new DataSetColumn("EXITS", "EXITS", Integer.class), String.valueOf(o[5]));
			row.addColumnValue(new DataSetColumn("POSETIVE_ADJUSTS", "POSETIVE_ADJUSTS", Integer.class),
					String.valueOf(o[6]));
			row.addColumnValue(new DataSetColumn("NEGATIVE_ADJUSTS", "NEGATIVE_ADJUSTS", Integer.class),
					String.valueOf(o[7]));
			row.addColumnValue(new DataSetColumn("CURRENT_BALANCE", "CURRENT_BALANCE", Integer.class),
					String.valueOf(o[8]));
			row.addColumnValue(new DataSetColumn("VALIDITY", "VALIDITY", Date.class), String.valueOf(o[9]));
			dataSet.addRow(row);
		}

		return dataSet;
	}

	// workaround to prevent NullPointerException when no data found.   :-)
	private void setEmptyData(MapDataSet dataSet) {

		DataSetRow row = new DataSetRow();
		row.addColumnValue(new DataSetColumn("FNM", "FNM", String.class), StringUtils.EMPTY);
		row.addColumnValue(new DataSetColumn("NAME", "NAME", String.class), StringUtils.EMPTY);
		row.addColumnValue(new DataSetColumn("OUTPUT_UNIT", "OUTPUT_UNIT", Integer.class), StringUtils.EMPTY);
		row.addColumnValue(new DataSetColumn("INITIAL_BALANCE", "INITIAL_BALANCE", Integer.class), StringUtils.EMPTY);
		row.addColumnValue(new DataSetColumn("ENTRANCES", "ENTRANCES", Integer.class), StringUtils.EMPTY);
		row.addColumnValue(new DataSetColumn("EXITS", "EXITS", Integer.class), StringUtils.EMPTY);
		row.addColumnValue(new DataSetColumn("POSETIVE_ADJUSTS", "POSETIVE_ADJUSTS", Integer.class), StringUtils.EMPTY);
		row.addColumnValue(new DataSetColumn("NEGATIVE_ADJUSTS", "NEGATIVE_ADJUSTS", Integer.class), StringUtils.EMPTY);
		row.addColumnValue(new DataSetColumn("CURRENT_BALANCE", "CURRENT_BALANCE", Integer.class), StringUtils.EMPTY);
		row.addColumnValue(new DataSetColumn("VALIDITY", "VALIDITY", Date.class), StringUtils.EMPTY);
		dataSet.addRow(row);
	}
}
