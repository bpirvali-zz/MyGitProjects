--  jdbc:h2:~/test;MODE=MySQL
CREATE SCHEMA DBO;
CREATE TABLE TEST 
(
	ID			INTEGER NOT NULL
	, NAME		VARCHAR(64)
	, PRIMARY KEY(ID)
);

--CREATE TABLE MainIndent 
--(
--	ShipId			VARCHAR(6)
--	, BDesc			VARCHAR(72)
--	, shipname		VARCHAR(50)
--	, Code			VARCHAR(5)
--	, SCode			VARCHAR(2)
--	, Ordy			VARCHAR(4)
--	, OrdM			VARCHAR(2)
--	, OrdD			VARCHAR(3)
--	, OnBr			VARCHAR(3)
--	, DOI			VARCHAR(10)
--	, Char			VARCHAR(1)
--	, OrDt			VARCHAR(10)
--	, Lstd			SMALLINT
--	, Inco			VARCHAR(15) NOT NULL
--	, Mstr			VARCHAR(45)
--	, Rnk			VARCHAR(30)
--	, IsNm			VARCHAR(45)
--	, RcvdDt		VARCHAR(10)
--	, RcvdPrt		VARCHAR(50)
--	, cancel		BIT
--	, Suplr			VARCHAR(100)
--	, extracted		BIT
--	, extorder		VARCHAR(35)
--	, Stts			VARCHAR(35)
--	, SndDt1		VARCHAR(10)
--	, Cpy2			VARCHAR(10)
--	, Cpy41			VARCHAR(10)
--	, Fin			SMALLINT
--	, DlvryRqrd		VARCHAR(12)
--	, SttsDt		VARCHAR(10)
--	, StsId			SMALLINT	
--	, PRIMARY KEY(Inco)
--);
--
--CREATE TABLE SubIndent 
--(
--	Inco			VARCHAR(15) NOT NULL
--	, Iinco			VARCHAR(25) NOT NULL
--	, Impa			VARCHAR(9)
--	, QttyRq		FLOAT
--	, Item			VARCHAR(69)
--	, Unit			VARCHAR(10)
--	, StStk			FLOAT
--	, ROB			FLOAT
--	, QttyRcv		FLOAT
--	, DRcvd			VARCHAR(10)
--	, Rcvd			SMALLINT
--	, amount		FLOAT
--	, prcvd			VARCHAR(30)
--	, INo			REAL
--	, OfRsd			SMALLINT
--	, BgIno			VARCHAR(50)			
--	, PRIMARY KEY(Iinco)
--);
--
--DROP TABLE DBO.SUBINDENT;

CREATE TABLE DBO.SUBINDENT 
( 
	ItId int IDENTITY(1,1) NOT NULL, 
	Inco nvarchar(15) NULL, 
	Iinco nvarchar(25) NULL, 
	Impa nvarchar(9) NULL, 
	QttyRq float NULL,           	
	Item nvarchar(69) NULL, 
	Unit nvarchar(10) NULL, 
	StStk float NULL,            	
	ROB float NULL,              	
	QttyAp float NULL, 
	QttyRcv float NULL,           	
	DRcvd nvarchar(10) NULL, 
	uprice float NULL,           	
	discnt float NULL,           	
	Rcvd smallint NULL,           	
	amount float NULL,           	
	ItmCan smallint NULL,            
	CanDt nvarchar(10) NULL, 
	icurrency nvarchar(5) NULL, 
	IAed smallint NULL,           	
	IAdel smallint NULL,           	
	prcvd nvarchar(30) NULL, 
	IUpDt smallint NULL,           	
	INo real NULL, 
	OfRsd smallint NULL,           	
	BgIno nvarchar(50) NULL, 
	SpNote nvarchar(65) NULL, 
	PRIMARY KEY(ItId)
);

-- DROP TABLE DBO.MAININDENT;

CREATE TABLE DBO.MAININDENT (
	id int IDENTITY(1,1) NOT NULL,
	ShipId nvarchar(6) NULL,
	BDesc nvarchar(72) NULL,
	shipname nvarchar(50) NULL,
	Code nvarchar(5) NULL,
	SCode nvarchar(2) NULL,
	OrdY nvarchar(4) NULL,
	OrdYp nvarchar(4) NULL,
	OrdM nvarchar(2) NULL,
	OrdD nvarchar(3) NULL,
	OnBr nvarchar(3) NULL,
	DOI nvarchar(10) NULL,
	Char nvarchar(1) NULL,
	OrDt nvarchar(10) NULL,
	Lstd smallint NULL,
	Inco nvarchar(15) NULL,
	Mstr nvarchar(45) NULL,
	Rnk nvarchar(30) NULL,
	IsNm nvarchar(45) NULL,
	RcvdDt nvarchar(10) NULL,
	RcvdPrt nvarchar(50) NULL,
	cancel bit NULL,            
	Suplr nvarchar(100) NULL,
	SuplrId nvarchar(4) NULL,
	extracted bit NULL,         
	InvNo nvarchar(30) NULL,
	extorder nvarchar(35) NULL,
	InvDt nvarchar(10) NULL,
	EstCost float NULL,         
	Crncy nvarchar(5) NULL,
	EstCostUsd real NULL,       
	TtlAmnt real NULL,          
	TtlAmntUsd real NULL,       
	Stts nvarchar(35) NULL,
	StsId smallint NULL,
	SttsDt nvarchar(10) NULL,
	SndDt nvarchar(10) NULL,
	Cpy2 nvarchar(10) NULL,
	Cpy4 nvarchar(10) NULL,
	Fin smallint NULL,          
	Aedt smallint NULL,         
	ADel smallint NULL,         
	DlvryRqrd nvarchar(12) NULL,
	UpDt smallint NULL,         
	DiDate nvarchar(10) NULL,
	DiPlace nvarchar(50) NULL,
	Curr nvarchar(5) NULL,      
	DeptId smallint NULL,
	PRIMARY KEY(ID)
)
