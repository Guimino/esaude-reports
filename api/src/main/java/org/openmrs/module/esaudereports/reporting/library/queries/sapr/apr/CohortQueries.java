package org.openmrs.module.esaudereports.reporting.library.queries.sapr.apr;

/**
 * Created by Hamilton Mutaquiha on 8/23/17.
 */
public class CohortQueries {
	
	/**
	 * PROGRAMA: PACIENTES TRANSFERIDOS DE NO PROGRAMA DE TRATAMENTO ARV: NUM PERIODO
	 */
	public static final String PATIENTS_TRANFERED_FROM_ON_ART_TRAEATMENT_PROGRAM_IN_PERIOD = "select 	pg.patient_id"
	        + " from 	patient p" + " inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
	        + " where 	pg.voided=0 and ps.voided=0 and p.voided=0 and"
	        + " pg.program_id=2 and ps.state=29 and ps.start_date=pg.date_enrolled and"
	        + " ps.start_date between :startDate and :endDate and location_id=:location";
	
	/**
	 * INICIO DE TRATAMENTO ARV - NUM PERIODO: INCLUI TRANSFERIDOS DE COM DATA DE INICIO CONHECIDA
	 * (SQL)
	 */
	public static final String ART_START_IN_PERIOD_INCLUDE_TRANSFERS_FROM = "select patient_id"
	        + " from"
	        + " (	Select patient_id,min(data_inicio) data_inicio"
	        + " 	from"
	        + " 	(	Select 	p.patient_id,min(e.encounter_datetime) data_inicio"
	        + " 		from 	patient p"
	        + " 		inner join encounter e on p.patient_id=e.patient_id"
	        + " 		inner join obs o on o.encounter_id=e.encounter_id"
	        + " 		where 	e.voided=0 and o.voided=0 and p.voided=0 and"
	        + " 		e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and"
	        + " 		e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 		group by p.patient_id"
	        + " 		union"
	        + " 		Select 	p.patient_id,min(value_datetime) data_inicio"
	        + " 		from 	patient p"
	        + " 		inner join encounter e on p.patient_id=e.patient_id"
	        + " 		inner join obs o on e.encounter_id=o.encounter_id"
	        + " 		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and"
	        + " 		o.concept_id=1190 and o.value_datetime is not null and"
	        + " 		o.value_datetime<=:endDate and e.location_id=:location"
	        + " 		group by p.patient_id"
	        + " 		union"
	        + " 		select 	pg.patient_id,date_enrolled data_inicio"
	        + " 		from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 		where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location"
	        + " 		union"
	        + " 		SELECT 	e.patient_id, MIN(e.encounter_datetime) AS data_inicio"
	        + " 		FROM 	patient p"
	        + " 		inner join encounter e on p.patient_id=e.patient_id"
	        + " 		WHERE	p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 		GROUP BY p.patient_id" + " 	) inicio_real" + " 	group by patient_id" + " )inicio"
	        + " where data_inicio between :startDate and :endDate";
	
	/**
	 * GRAVIDAS INSCRITAS NO SERVIÇO TARV
	 */
	public static final String PREGNANTS_INSCRIBED_ON_ART_SERVICE = "Select 	p.patient_id"
	        + " from 	patient p"
	        + " 		inner join encounter e on p.patient_id=e.patient_id"
	        + " 		inner join obs o on e.encounter_id=o.encounter_id"
	        + " where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=44 and"
	        + " 		e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location"
	        + " union"
	        + " Select 	p.patient_id"
	        + " from 	patient p inner join encounter e on p.patient_id=e.patient_id"
	        + " 		inner join obs o on e.encounter_id=o.encounter_id"
	        + " where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and"
	        + " 		e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location"
	        + " union"
	        + " Select 	p.patient_id"
	        + " from 	patient p inner join encounter e on p.patient_id=e.patient_id"
	        + " 		inner join obs o on e.encounter_id=o.encounter_id"
	        + " where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and"
	        + " 		e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location"
	        + " union" + " select 	pp.patient_id" + " from 	patient_program pp"
	        + " where 	pp.program_id=8 and pp.voided=0 and"
	        + " 		pp.date_enrolled between :startDate and :endDate and pp.location_id=:location";
	
	/**
	 * PROGRAMA: PACIENTES QUE DERAM PARTO HÁ DOIS ANOS ATRÁS DA DATA DE REFERENCIA - LACTANTES
	 */
	public static final String PATIENTS_WHO_GAVE_BIRTH_TWO_YEARS_AGO = "select 	pg.patient_id"
	        + " from 	patient p"
	        + " 		inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 		inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
	        + " where 	pg.voided=0 and ps.voided=0 and p.voided=0 and"
	        + " 		pg.program_id=8 and ps.state=27 and ps.end_date is null and"
	        + " 		ps.start_date between date_add(:startDate, interval -2 year) and date_add(:startDate, interval -1 day) and location_id=:location";
	
	/**
	 * PROGRAMA: PACIENTES INSCRITOS NO PROGRAMA TRATAMENTO ARV (TARV) - PERIODO FINAL
	 */
	public static final String PATIENTS_INSCRIBED_ON_ART_PROGRAM = "select 	pg.patient_id"
	        + " from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location";
	
	/**
	 * PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV: PERIODO FINAL
	 */
	public static final String PATIENTS_WHO_LEFT_ART_PROGRAM = "select 	pg.patient_id" + " from 	patient p"
	        + " inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
	        + " where 	pg.voided=0 and ps.voided=0 and p.voided=0 and" + " pg.program_id=2 and ps.state in (7,8,9,10)"
	        + " and ps.end_date is null" + " and ps.start_date<=:endDate and location_id=:location";
	
	/**
	 * PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV: PERIODO FINAL
	 */
	public static final String LOST_TO_FOLLOW_UP = "select patient_id"
	        + " from"
	        + " (	Select 	p.patient_id,max(encounter_datetime) encounter_datetime"
	        + " 	from 	patient p"
	        + " 	inner join encounter e on e.patient_id=p.patient_id"
	        + " 	where 	p.voided=0 and e.voided=0 and e.encounter_type=18"
	        + " 	and e.location_id=:location and e.encounter_datetime<=:endDate"
	        + " 	group by p.patient_id"
	        + " ) max_frida"
	        + " inner join obs o on o.person_id=max_frida.patient_id"
	        + " where 	max_frida.encounter_datetime=o.obs_datetime and o.voided=0 and o.concept_id=5096 and o.location_id=:location"
	        + " and patient_id not in"
	        + " (	select 	pg.patient_id"
	        + " 	from 	patient p"
	        + " 	inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 	inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
	        + " 	where 	pg.voided=0 and ps.voided=0 and p.voided=0 and"
	        + " 	pg.program_id=2 and ps.state in (7,8,9,10) and ps.end_date is null and"
	        + " 	ps.start_date<=:endDate and location_id=:location"
	        + " 	union"
	        + " 	select patient_id"
	        + " 	from"
	        + " 	(	Select 	p.patient_id,max(encounter_datetime) encounter_datetime"
	        + " 		from 	patient p"
	        + " 		inner join encounter e on e.patient_id=p.patient_id"
	        + " 		where 	p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and"
	        + " 		e.location_id=:location and e.encounter_datetime<=:endDate"
	        + " 		group by p.patient_id"
	        + " 	) max_mov"
	        + " 	inner join obs o on o.person_id=max_mov.patient_id"
	        + " 	where 	max_mov.encounter_datetime=o.obs_datetime and o.voided=0 and"
	        + " 	o.concept_id=1410 and o.location_id=:location and datediff(:endDate,o.value_datetime)<=60"
	        + " 	union"
	        + " 	select abandono.patient_id"
	        + " 	from"
	        + " 	("
	        + " 		select 	pg.patient_id"
	        + " 		from 	patient p"
	        + " 		inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 		inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
	        + " 		where 	pg.voided=0 and ps.voided=0 and p.voided=0 and"
	        + " 		pg.program_id=2 and ps.state=9 and ps.end_date is null and"
	        + " 		ps.start_date<=:endDate and location_id=:location"
	        + " 	)abandono"
	        + " 	inner join"
	        + " 	("
	        + " 		select max_frida.patient_id,max_frida.encounter_datetime,o.value_datetime"
	        + " 		from"
	        + " 		(	Select 	p.patient_id,max(encounter_datetime) encounter_datetime"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on e.patient_id=p.patient_id"
	        + " 			where 	p.voided=0 and e.voided=0 and e.encounter_type=18 and"
	        + " 			e.location_id=:location and e.encounter_datetime<=:endDate"
	        + " 			group by p.patient_id"
	        + " 		) max_frida"
	        + " 		inner join obs o on o.person_id=max_frida.patient_id"
	        + " 		where 	max_frida.encounter_datetime=o.obs_datetime and o.voided=0 and o.concept_id=5096 and o.location_id=:location"
	        + " 	) ultimo_fila on abandono.patient_id=ultimo_fila.patient_id"
	        + " 	where datediff(:endDate,ultimo_fila.value_datetime)<60" + " )"
	        + " and datediff(:endDate,o.value_datetime)>60";
	
	/**
	 * PACIENTES ACTIVOS NO GAAC ATÉ UM DETERMINADO PERIODO
	 */
	public static final String PATIENTS_ACTIVE_ON_GAAC = "Select gm.member_id"
	        + " from gaac g inner join gaac_member gm on g.gaac_id=gm.gaac_id"
	        + " where gm.start_date<:endDate and gm.voided=0 and g.voided=0"
	        + " and ((leaving is null) or (leaving=0) or (leaving=1 and gm.end_date>:endDate))"
	        + " and location_id=:location";
	
	/**
	 * PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV - OBITOU: PERIODO FINAL
	 */
	public static final String PATIENTS_WHO_PASSED_AWAY = "select 	pg.patient_id" + " from 	patient p"
	        + " inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
	        + " where 	pg.voided=0 and ps.voided=0 and p.voided=0 and"
	        + " pg.program_id=2 and ps.state=10 and ps.end_date is null and"
	        + " ps.start_date<=:endDate and location_id=:location";
	
	/**
	 * PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV - ABANDONO: PERIODO FINAL
	 */
	public static final String PATIENTS_WHO_ABANDONED = "select 	pg.patient_id" + " from 	patient p"
	        + " inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
	        + " where 	pg.voided=0 and ps.voided=0 and p.voided=0 and"
	        + " pg.program_id=2 and ps.state=9 and ps.end_date is null and"
	        + " ps.start_date<=:endDate and location_id=:location";
	
	/**
	 * PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV - TRANSFERIDO PARA: PERIODO
	 * FINAL
	 */
	public static final String PATIENTS_TRANSFERED_TO = "select 	pg.patient_id" + " from 	patient p"
	        + " inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
	        + " where 	pg.voided=0 and ps.voided=0 and p.voided=0 and"
	        + " pg.program_id=2 and ps.state=7 and ps.end_date is null and"
	        + " ps.start_date<=:endDate and location_id=:location";
	
	/**
	 * PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV - SUSPENSO: PERIODO FINAL
	 */
	public static final String PATIENTS_SUSPENDED_ON_TREATMENT = "select 	pg.patient_id" + " from 	patient p"
	        + " inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
	        + " where 	pg.voided=0 and ps.voided=0 and p.voided=0 and"
	        + " pg.program_id=2 and ps.state=8 and ps.end_date is null and"
	        + " ps.start_date<=:endDate and location_id=:location";
	
	/**
	 * CRIANCAS DE 0-14 ANOS QUE INCIARAM TARV - IDADE NO INICIO DE TARV
	 */
	public static final String CHILDREN_0_14_YEAR_STARTED_ART = "select patient_id"
	        + " 	from"
	        + " 	(select patient_id,min(data_inicio) data_inicio"
	        + " 		from"
	        + " 		("
	        + " 			Select 	p.patient_id,min(e.encounter_datetime) data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + "			inner join obs o on o.encounter_id=e.encounter_id"
	        + " 			where 	e.voided=0 and o.voided=0 and p.voided=0 and"
	        + " 			e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and"
	        + " 			e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			Select p.patient_id,min(value_datetime) data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and"
	        + " 			o.concept_id=1190 and o.value_datetime is not null and"
	        + " 			o.value_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			select 	pg.patient_id,date_enrolled as data_inicio"
	        + " 			from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 			where 	pg.voided=0 and p.voided=0 and program_id=2 and"
	        + " 			pg.date_enrolled<=:endDate and pg.location_id=:location"
	        + " union"
	        + " select p.patient_id,min(encounter_datetime)"
	        + " from patient p inner join encounter e on p.patient_id=e.patient_id"
	        + " where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " group by p.patient_id" + " 		) inicio" + " 	group by patient_id"
	        + " 	)inicio_real inner join person pe on inicio_real.patient_id=pe.person_id"
	        + " 	where (datediff(data_inicio,birthdate)/365)<15";
	
	/**
	 * ADULTOS DE 15+ ANOS QUE INICIARAM TARV - IDADE NO INICIO DE TARV
	 */
	public static final String ADULTS_15_PLUS_YEAR_STARTED_ART = "select patient_id"
	        + " 	from"
	        + " 	(select patient_id,min(data_inicio) data_inicio"
	        + " 		from"
	        + " 		("
	        + " 			Select 	p.patient_id,min(e.encounter_datetime) data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + "			inner join obs o on o.encounter_id=e.encounter_id"
	        + " 			where 	e.voided=0 and o.voided=0 and p.voided=0 and"
	        + " 			e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and"
	        + " 			e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			Select p.patient_id,min(value_datetime) data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and"
	        + " 			o.concept_id=1190 and o.value_datetime is not null and"
	        + " 			o.value_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			select 	pg.patient_id,date_enrolled as data_inicio"
	        + " 			from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 			where 	pg.voided=0 and p.voided=0 and program_id=2 and"
	        + " 			pg.date_enrolled<=:endDate and pg.location_id=:location"
	        + " union"
	        + " select p.patient_id,min(encounter_datetime)"
	        + " from patient p inner join encounter e on p.patient_id=e.patient_id"
	        + " where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " group by p.patient_id" + " 		) inicio" + " 	group by patient_id"
	        + " 	)inicio_real inner join person pe on inicio_real.patient_id=pe.person_id"
	        + " 	where (datediff(data_inicio,birthdate)/365)>=15";
	
	/**
	 * PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV - SUSPENSO, ABANDONO, OBITO,
	 * TRANSFERIDO PARA: PERIODO FINAL
	 */
	public static final String PATIENTS_WHO_LEFT_ART_PROGRAM_STATES = "select 	pg.patient_id" + " from 	patient p"
	        + " inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
	        + " where 	pg.voided=0 and ps.voided=0 and p.voided=0 and"
	        + " pg.program_id=2 and ps.state in (:state) and ps.end_date is null and"
	        + " ps.start_date<=:endDate and location_id=:location";
	
	/**
	 * FALHAS CLINICAS - SQL
	 */
	public static final String CLINIC_TREATMENT_FAILURE = "Select ESTADIO1.patient_id"
	        + " from"
	        + " (	Select estadio_antes.patient_id,estadio_antes.data_inicio,estadio_antes.data_estadio_antes,obs.value_coded as valor_estadio_antes"
	        + " 	from"
	        + " 	(select patient_id,max(data_inicio) as data_inicio,max(obs_datetime) data_estadio_antes"
	        + " 	from"
	        + " 		(	Select 	p.patient_id,min(encounter_datetime)  data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 			where 	o.concept_id=1255 and o.value_coded in (1256,1369) and"
	        + " 			e.voided=0 and o.voided=0 and p.voided=0 and"
	        + " 			e.encounter_type in (18,6,9) and e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			Select p.patient_id,min(value_datetime) data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and"
	        + " 			o.concept_id=1190 and o.value_datetime is not null and"
	        + " 			o.value_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			select 	pg.patient_id,date_enrolled data_inicio"
	        + " 			from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 			where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location"
	        + " 		) inicio,"
	        + " 		(	Select 	o.person_id,o.obs_datetime"
	        + " 			from 	obs o"
	        + " 			where 	o.concept_id=5356 and o.voided=0 and o.obs_datetime<=:endDate and o.location_id=:location"
	        + " 		) estadio"
	        + " 	where 	estadio.person_id=inicio.patient_id and estadio.obs_datetime<inicio.data_inicio"
	        + " 	group by patient_id"
	        + " 	) estadio_antes,"
	        + " 	obs"
	        + " 	where obs.person_id=estadio_antes.patient_id and obs.concept_id=5356 and obs.obs_datetime=estadio_antes.data_estadio_antes and obs.voided=0 and obs.location_id=:location"
	        + " 	) ESTADIO1,"
	        + " 	(select estadio_depois.patient_id,estadio_depois.data_inicio,estadio_depois.data_estadio_depois,obs.value_coded as valor_estadio_depois"
	        + " 	from"
	        + " 	(select patient_id,max(data_inicio) as data_inicio,max(obs_datetime) data_estadio_depois"
	        + " 	from"
	        + " 		(	Select 	p.patient_id,min(encounter_datetime)  data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 			where 	o.concept_id=1255 and o.value_coded in (1256,1369) and"
	        + " 			e.voided=0 and o.voided=0 and p.voided=0 and"
	        + " 			e.encounter_type in (18,6,9) and e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			Select p.patient_id,min(value_datetime) data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and"
	        + " 			o.concept_id=1190 and o.value_datetime is not null and"
	        + " 			o.value_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			select 	pg.patient_id,date_enrolled data_inicio"
	        + " 			from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 			where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location"
	        + " 		) inicio,"
	        + " 		(	Select 	o.person_id,o.obs_datetime"
	        + " 			from 	obs o"
	        + " 			where 	o.concept_id=5356 and o.voided=0 and o.obs_datetime<=:endDate and o.location_id=:location"
	        + " 		) estadio"
	        + " 		where estadio.person_id=inicio.patient_id and estadio.obs_datetime>date_add(inicio.data_inicio,interval 6 month)"
	        + " 		group by patient_id"
	        + " 		) estadio_depois,"
	        + " 		obs"
	        + " 		where obs.person_id=estadio_depois.patient_id and obs.concept_id=5356 and obs.obs_datetime=estadio_depois.data_estadio_depois and obs.voided=0 and obs.location_id=:location"
	        + " 	) ESTADIO2,"
	        + " 	person"
	        + " 	where"
	        + " 	ESTADIO1.patient_id=ESTADIO2.patient_id and"
	        + " 	ESTADIO1.patient_id=person.person_id and"
	        + " 	("
	        + " 		(round(datediff(:endDate,birthdate)/365)<=14 and valor_estadio_antes<=1205 and valor_estadio_depois>=1206) or"
	        + " 		(round(datediff(:endDate,birthdate)/365)>=15 and valor_estadio_antes<=1206 and valor_estadio_depois=1207)"
	        + " 	) and valor_estadio_depois>valor_estadio_antes";
	
	/**
	 * FALHAS IMUNOLOGICAS - SQL
	 */
	public static final String IMUNOLOGIC_TREATMENT_FAILURE = "Select CD41.patient_id"
	        + " from"
	        + " 	(Select CD4_ANTES.patient_id,CD4_ANTES.data_inicio,CD4_ANTES.data_cd4_antes,obs.value_numeric as valor_cd4_antes"
	        + " 	from"
	        + " 	(select patient_id,max(data_inicio) data_inicio,max(obs_datetime) as data_cd4_antes"
	        + " 	from"
	        + " 		(	Select 	p.patient_id,min(encounter_datetime)  data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 			where 	o.concept_id=1255 and o.value_coded=1256 and"
	        + " 			e.voided=0 and o.voided=0 and p.voided=0 and"
	        + " 			e.encounter_type in (18,6,9) and e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			Select p.patient_id,min(value_datetime) data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and"
	        + " 			o.concept_id=1190 and o.value_datetime is not null and"
	        + " 			o.value_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			select 	pg.patient_id,date_enrolled data_inicio"
	        + " 			from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 			where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location"
	        + " 		) inicio,"
	        + " 		(	Select 	o.person_id,o.obs_datetime,o.value_numeric"
	        + " 			from 	obs o"
	        + " 			where 	o.concept_id=5497 and o.voided=0 and o.obs_datetime<=:endDate and o.location_id=:location"
	        + " 		) cd4"
	        + " 	where 	cd4.person_id=inicio.patient_id and cd4.obs_datetime<inicio.data_inicio"
	        + " 	group by patient_id) CD4_ANTES,"
	        + " 	obs"
	        + " 	where obs.person_id=CD4_ANTES.patient_id and obs.concept_id=5497 and obs.obs_datetime=CD4_ANTES.data_cd4_antes and obs.voided=0 and obs.location_id=:location"
	        + " 	) CD41,"
	        + " 	(Select CD4_DEPOIS.patient_id,CD4_DEPOIS.data_inicio,CD4_DEPOIS.data_cd4_depois,obs.value_numeric as valor_cd4_depois,CD4_DEPOIS.cd4_pico"
	        + " 	from"
	        + " 	(select patient_id,max(data_inicio) as data_inicio,max(obs_datetime) data_cd4_depois,max(value_numeric) cd4_pico"
	        + " 	from"
	        + " 		(	Select 	p.patient_id,min(encounter_datetime)  data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 			where 	o.concept_id=1255 and o.value_coded=1256 and"
	        + " 			e.voided=0 and o.voided=0 and p.voided=0 and"
	        + " 			e.encounter_type=18 and e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + "			Select p.patient_id,min(value_datetime) data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and"
	        + " 			o.concept_id=1190 and o.value_datetime is not null and"
	        + " 			o.value_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			select 	pg.patient_id,date_enrolled data_inicio"
	        + " 			from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 			where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location"
	        + " 		) inicio,"
	        + " 		(	Select 	o.person_id,o.obs_datetime,o.value_numeric"
	        + " 			from 	obs o"
	        + " 			where 	o.concept_id=5497 and o.voided=0 and o.obs_datetime<=:endDate and o.location_id=:location"
	        + " 		) cd4"
	        + " 		where cd4.person_id=inicio.patient_id and cd4.obs_datetime>date_add(inicio.data_inicio,interval 6 month)"
	        + " 		group by patient_id"
	        + " 		) CD4_DEPOIS,"
	        + " 		obs"
	        + " 		where obs.person_id=CD4_DEPOIS.patient_id and obs.concept_id=5497 and obs.obs_datetime=CD4_DEPOIS.data_cd4_depois and obs.voided=0 and obs.location_id=:location"
	        + " 		) CD42,"
	        + " 		person"
	        + " 		where CD41.patient_id=CD42.patient_id and person.person_id=CD41.patient_id and"
	        + " 		("
	        + " 			(round(datediff(:endDate,birthdate)/365)<=4 and valor_cd4_antes<200 and valor_cd4_depois<200) or"
	        + " 			(round(datediff(:endDate,birthdate)/365) between 5 and 14 and ((valor_cd4_antes<100 and valor_cd4_depois<100) or (valor_cd4_depois<valor_cd4_antes))) or"
	        + " 			(round(datediff(:endDate,birthdate)/365)>=15 and ((valor_cd4_antes<100 and valor_cd4_depois<100) or (valor_cd4_depois<valor_cd4_antes) or (valor_cd4_depois<(cd4_pico/2))))"
	        + " 		)";
	
	/**
	 * PACIENTES QUE ESTAO A MAIS DE 6 MESES EM TARV
	 */
	public static final String PATIENTS_IN_ART_FOR_MORE_THAN_6_MONTHS = "select patient_id"
	        + " from"
	        + " (	select patient_id,min(data_inicio) data_inicio"
	        + " 		from"
	        + " 		("
	        + " 			Select 	p.patient_id,min(e.encounter_datetime) data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on o.encounter_id=e.encounter_id"
	        + " 			where 	e.voided=0 and o.voided=0 and p.voided=0 and"
	        + " 			e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and "
	        + " 			e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			Select p.patient_id,min(value_datetime) data_inicio"
	        + " 			from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and"
	        + " 			o.concept_id=1190 and o.value_datetime is not null and"
	        + " 			o.value_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id"
	        + " 			union"
	        + " 			select 	pg.patient_id,date_enrolled as data_inicio"
	        + " 			from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 			where 	pg.voided=0 and p.voided=0 and program_id=2 and"
	        + " 			pg.date_enrolled<=:endDate and pg.location_id=:location"
	        + " 			union"
	        + " 			select 	p.patient_id,min(encounter_datetime) as data_inicio"
	        + " 			from 	patient p inner join encounter e on p.patient_id=e.patient_id"
	        + " 			where 	p.voided=0 and e.voided=0 and e.encounter_type=18 and e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 			group by p.patient_id" + " 		) inicio" + " 	group by patient_id" + " )inicio_real"
	        + " where timestampdiff (MONTH,data_inicio,:endDate)>=6";
	
	/**
	 * PACIENTES COM RESULTADO DE CARGA VIRAL NOS ULTIMOS 12 MESES
	 */
	public static final String PATIENTS_WITH_VIRAL_LOAD_RESULTS_LAST_12_MONTHS = "Select 	p.patient_id"
	        + " from 	patient p"
	        + " inner join encounter e on p.patient_id=e.patient_id"
	        + " inner join obs o on e.encounter_id=o.encounter_id"
	        + " where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,6,9) and"
	        + " o.concept_id=856 and o.value_numeric is not null and"
	        + " e.encounter_datetime between date_add(:endDate, interval -12 MONTH) and :endDate and e.location_id=:location";
	
	/**
	 * PACIENTES COM CARGA VIRAL DETECTAVEL NOS ULTIMOS 12 MESESgit
	 */
	public static final String PATIENTS_WITH_DETECTABLE_VIRAL_LOAD_LAST_12_MONTHS = "Select ultima_carga.patient_id"
	        + " from"
	        + " (Select 	p.patient_id,max(o.obs_datetime) data_carga"
	        + " from 	patient p"
	        + " inner join encounter e on p.patient_id=e.patient_id"
	        + " inner join obs o on e.encounter_id=o.encounter_id"
	        + " where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,6,9) and"
	        + " o.concept_id=856 and o.value_numeric is not null and"
	        + " e.encounter_datetime between date_add(:endDate, interval -12 MONTH) and :endDate and e.location_id=:location"
	        + " group by p.patient_id" + " ) ultima_carga"
	        + " inner join obs on obs.person_id=ultima_carga.patient_id and obs.obs_datetime=ultima_carga.data_carga"
	        + " where 	obs.voided=0 and obs.concept_id=856 and obs.location_id=:location and obs.value_numeric>=1000";
	
	/**
	 * PACIENTES COM CARGA VIRAL INDETECTAVEL NOS ULTIMOS 12 MESES
	 */
	public static final String PATIENTS_WITH_UNDETECTABLE_VIRAL_LOAD_LAST_12_MONTHS = "Select ultima_carga.patient_id"
	        + " from"
	        + " (Select 	p.patient_id,max(o.obs_datetime) data_carga"
	        + " from 	patient p"
	        + " inner join encounter e on p.patient_id=e.patient_id"
	        + " inner join obs o on e.encounter_id=o.encounter_id"
	        + " where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,6,9) and"
	        + " o.concept_id=856 and o.value_numeric is not null and"
	        + " e.encounter_datetime between date_add(:endDate, interval -12 MONTH) and :endDate and e.location_id=:location"
	        + " group by p.patient_id" + " ) ultima_carga"
	        + " inner join obs on obs.person_id=ultima_carga.patient_id and obs.obs_datetime=ultima_carga.data_carga"
	        + " where 	obs.voided=0 and obs.concept_id=856 and obs.location_id=:location and obs.value_numeric<1000";
	
	/**
	 * PROGRAMA: PACIENTES INSCRITOS NO PROGRAMA DE TUBERCULOSE - NUM PERIODO
	 */
	public static final String PATIENTS_INSCRIBED_ON_TB_PROGRAM = "select 	pg.patient_id"
	        + " from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " where 	pg.voided=0 and p.voided=0 and program_id=5 "
	        + "and date_enrolled between :startDate and :endDate and location_id=:location";;
}
