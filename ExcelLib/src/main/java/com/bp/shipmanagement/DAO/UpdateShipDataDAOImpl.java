package com.bp.shipmanagement.DAO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
/*
changed fields:
- SndDt1 --> SndDt
- Cpy41  --> Cpy4
 * 
 * */
public class UpdateShipDataDAOImpl implements UpdateShipDataDAO {
	@Inject
	private NamedParameterJdbcTemplate npJdbcTemplate;
	@Inject
	private JdbcTemplate jdbcTemplate;

	@Override
	public Set<String> getMainIndentPKs() {
		String sql = "SELECT Inco FROM dbo.MainIndent";
		List<String> l = jdbcTemplate.queryForList(sql, String.class);
		Set<String> Incos = new HashSet<String>(l);
		return Incos;
	}

	@Override
	public Set<String> getSubIndentPKs() {
		String sql = "SELECT Iinco FROM dbo.SubIndent";
		List<String> l = jdbcTemplate.queryForList(sql, String.class);
		Set<String> Iincos = new HashSet<String>(l);
		return Iincos;
	}

	@Override
	public void insertMainIndent(List<String> row) {
		String sql = "INSERT INTO dbo.MainIndent "
				+ "(ShipId, BDesc, shipname, Code, SCode, Ordy, OrdM, OrdD, OnBr, DOI, Char, OrDt, Lstd, Inco, Mstr, Rnk, IsNm, RcvdDt, RcvdPrt, cancel, Suplr, extracted, extorder, Stts, SndDt, Cpy2, Cpy4, Fin, DlvryRqrd, SttsDt, StsId) "
				+ "VALUES (:ShipId, :BDesc, :shipname, :Code, :SCode, :Ordy, :OrdM, :OrdD, :OnBr, :DOI, :Char, :OrDt, :Lstd, :Inco, :Mstr, :Rnk, :IsNm, :RcvdDt, :RcvdPrt, :cancel, :Suplr, :extracted, :extorder, :Stts, :SndDt, :Cpy2, :Cpy4, :Fin, :DlvryRqrd, :SttsDt, :StsId)";

		npJdbcTemplate.update(sql, papulateMainIndentMap(row));
	}

	@Override
	public void updateMainIndent(List<String> row) {
		String sql = "UPDATE dbo.MainIndent SET " + "ShipId=:ShipId,"
				+ "BDesc=:BDesc," + "shipname=:shipname," + "Code=:Code,"
				+ "SCode=:SCode," + "Ordy=:Ordy," + "OrdM=:OrdM,"
				+ "OrdD=:OrdD," + "OnBr=:OnBr," + "DOI=:DOI," + "Char=:Char,"
				+ "OrDt=:OrDt," + "Lstd=:Lstd," + "Mstr=:Mstr," + "Rnk=:Rnk,"
				+ "IsNm=:IsNm," + "RcvdDt=:RcvdDt," + "RcvdPrt=:RcvdPrt,"
				+ "cancel=:cancel," + "Suplr=:Suplr," + "extracted=:extracted,"
				+ "extorder=:extorder," + "Stts=:Stts," + "SndDt=:SndDt,"
				+ "Cpy2=:Cpy2," + "Cpy4=:Cpy4," + "Fin=:Fin,"
				+ "DlvryRqrd=:DlvryRqrd," + "SttsDt=:SttsDt," + "StsId=:StsId "
				+ "WHERE Inco=:Inco";

		npJdbcTemplate.update(sql, papulateMainIndentMap(row));
	}
	
	@Override
	public void insertSubIndent(List<String> row) {
		String sql = "INSERT INTO dbo.SubIndent "
				+ "(Inco, Iinco, Impa, QttyRq, Item, Unit, StStk, ROB, QttyRcv, DRcvd, Rcvd, amount, prcvd, INo, OfRsd, BgIno) "
				+ "VALUES (:Inco, :Iinco, :Impa, :QttyRq, :Item, :Unit, :StStk, :ROB, :QttyRcv, :DRcvd, :Rcvd, :amount, :prcvd, :INo, :OfRsd, :BgIno)";

		npJdbcTemplate.update(sql, papulateSubIndentMap(row));
	}

	@Override
	public void updateSubIndent(List<String> row) {
		String sql = "UPDATE dbo.SubIndent SET " + 
				"Inco=:Inco, " + 
				"Impa=:Impa, " + 
				"QttyRq=:QttyRq, " + 
				"Item=:Item, " + 
				"Unit=:Unit, " + 
				"StStk=:StStk, " + 
				"ROB=:ROB, " + 
				"QttyRcv=:QttyRcv, " + 
				"DRcvd=:DRcvd, " + 
				"Rcvd=:Rcvd, " + 
				"amount=:amount, " + 
				"prcvd=:prcvd, " + 
				"INo=:INo, " + 
				"OfRsd=:OfRsd, " + 
				"BgIno=:BgIno " + 
				"WHERE Iinco=:Iinco";
		
		npJdbcTemplate.update(sql, papulateSubIndentMap(row));
	}

	private Map<String, Object> papulateMainIndentMap(List<String> row) {
		int i = 0;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ShipId", row.get(i++));
		parameters.put("BDesc", row.get(i++));
		parameters.put("shipname", row.get(i++));
		parameters.put("Code", row.get(i++));
		parameters.put("SCode", row.get(i++));
		parameters.put("Ordy", row.get(i++));
		parameters.put("OrdM", row.get(i++));
		parameters.put("OrdD", row.get(i++));
		parameters.put("OnBr", row.get(i++));
		parameters.put("DOI", row.get(i++));
		parameters.put("Char", row.get(i++));
		parameters.put("OrDt", row.get(i++));
		parameters.put("Lstd",
				(row.get(i) != null ? (int) (Double.parseDouble(row.get(i++)))
						: new Integer(null)));
		parameters.put("Inco", row.get(i++));
		parameters.put("Mstr", row.get(i++));
		parameters.put("Rnk", row.get(i++));
		parameters.put("IsNm", row.get(i++));
		parameters.put("RcvdDt", row.get(i++));
		parameters.put("RcvdPrt", row.get(i++));
		parameters.put("cancel", row.get(i++));
		parameters.put("Suplr", row.get(i++));
		parameters.put("extracted", row.get(i++));
		parameters.put("extorder", row.get(i++));
		parameters.put("Stts", row.get(i++));
		parameters.put("SndDt", row.get(i++));
		parameters.put("Cpy2", row.get(i++));
		parameters.put("Cpy4", row.get(i++));
		parameters.put("Fin",
				(row.get(i) != null ? (int) (Double.parseDouble(row.get(i++)))
						: new Integer(null)));
		parameters.put("DlvryRqrd", row.get(i++));
		parameters.put("SttsDt", row.get(i++));
		parameters.put("StsId",
				(row.get(i) != null ? (int) (Double.parseDouble(row.get(i++)))
						: new Integer(null)));
		return parameters;
	}

	// 2013_1214_001_A	2013_1214_001_A0001	Sima066	1	CASTROL  LMX(GREASES)/Castrol	DRM	0	0	0		1	0		1	1	0001
	private Map<String, Object> papulateSubIndentMap(List<String> row) {
		int i = 0;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("Inco", row.get(i++));
		parameters.put("Iinco", row.get(i++));
		parameters.put("Impa", row.get(i++));
		parameters.put("QttyRq", 
				(row.get(i) != null ? Double.parseDouble(row.get(i++)) : new Double(null)));
		parameters.put("Item", row.get(i++));
		parameters.put("Unit", row.get(i++));
		parameters.put("StStk", 
				(row.get(i) != null ? Double.parseDouble(row.get(i++)) : new Double(null)));
		parameters.put("ROB", 
				(row.get(i) != null ? Double.parseDouble(row.get(i++)) : new Double(null)));
		parameters.put("QttyRcv", 
				(row.get(i) != null ? Double.parseDouble(row.get(i++)) : new Double(null)));
		parameters.put("DRcvd", row.get(i++));
		parameters.put("Rcvd", 
				(row.get(i) != null ? (int) (Double.parseDouble(row.get(i++))) : new Integer(null)));
		parameters.put("amount", 
				(row.get(i) != null ? Double.parseDouble(row.get(i++)) : new Double(null)));
		parameters.put("prcvd", row.get(i++));
		parameters.put("INo", 
				(row.get(i) != null ? Double.parseDouble(row.get(i++)) : new Double(null)));
		parameters.put("OfRsd", 
				(row.get(i) != null ? (int) (Double.parseDouble(row.get(i++))) : new Integer(null)));
		parameters.put("BgIno", row.get(i++));
		return parameters;
	}
}
