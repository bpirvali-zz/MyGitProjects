INSERT INTO TEST VALUES (0,'A');
INSERT INTO TEST VALUES (1,'B');
INSERT INTO TEST VALUES (2,'C');
INSERT INTO TEST VALUES (3,'D');
INSERT INTO TEST VALUES (4,'E');
INSERT INTO TEST VALUES (5,'F');
INSERT INTO TEST VALUES (6,'G');
INSERT INTO TEST VALUES (7,'H');
INSERT INTO TEST VALUES (8,'I');
INSERT INTO TEST VALUES (9,'J');

INSERT INTO MainIndent(ShipId, BDesc, shipname, Code, SCode, Ordy, OrdM, OrdD, OnBr, DOI, Char, OrDt, Lstd, Inco, Mstr, Rnk, IsNm, RcvdDt, RcvdPrt, cancel, Suplr, extracted, extorder, Stts, SndDt1, Cpy2, Cpy41, Fin, DlvryRqrd, SttsDt, StsId)
VALUES('1214','LUBRICATING OIL AND GREASE, Engine','Sea Angel','11101','02','2013','11','308','001','2013/04/11','A','2013/04/11',2.0,'2013_1214_001_A','Amir Mousavi','MASTER','Amir Mousavi',null,null,false,null,false,'2013_1214_001_A','New Raised (Vsl)','2013/11/04',null,null,1.0,'Earliest','2013/11/04',1.0);

INSERT INTO SubIndent(Inco, Iinco, Impa, QttyRq, Item, Unit, StStk, ROB, QttyRcv, DRcvd, Rcvd, amount, prcvd, INo, OfRsd, BgIno)
VALUES('2013_1214_001_A','2013_1214_001_A0001','Sima066',1.0,'CASTROL  LMX(GREASES)/Castrol','DRM',0.0,0.0,0.0,null,1.0,0.0,null,1.0,1.0,'0001');
