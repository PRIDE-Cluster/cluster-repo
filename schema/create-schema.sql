-- Generated by Oracle SQL Developer Data Modeler 4.0.3.853
--   at:        2015-07-08 15:03:07 BST
--   site:      Oracle Database 11g
--   type:      Oracle Database 11g




CREATE TABLE assay
(
  assay_pk          INTEGER NOT NULL ,
  assay_accession   VARCHAR2 (50 CHAR) NOT NULL ,
  project_accession VARCHAR2 (20 CHAR) NOT NULL ,
  project_title     VARCHAR2 (500 CHAR) ,
  assay_title       VARCHAR2 (500 CHAR) ,
  species           VARCHAR2 (250 CHAR) ,
  multi_species     CHAR (1) NOT NULL ,
  taxonomy_id       VARCHAR2 (250 CHAR) ,
  disease           VARCHAR2 (250 CHAR) ,
  tissue            VARCHAR2 (250 CHAR) ,
  search_engine     VARCHAR2 (500 CHAR) ,
  instrument        VARCHAR2 (250 CHAR) ,
  instrument_type   VARCHAR2 (250 CHAR) ,
  biomedical        CHAR (1) NOT NULL
)
TABLESPACE PRIDECLUS_TAB LOGGING ;
CREATE UNIQUE INDEX assay_accession_IDX ON assay
(
  assay_accession ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX assay_project_IDX ON assay
(
  project_accession ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX assay_species_IDX ON assay
(
  species ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX assay_tax_id_IDX ON assay
(
  taxonomy_id ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX assay_search_engine_IDX ON assay
(
  search_engine ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX assay_instrut_type_IDX ON assay
(
  instrument_type ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX assay_instrut_IDX ON assay
(
  instrument ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
ALTER TABLE assay ADD CONSTRAINT assay_PK PRIMARY KEY ( assay_pk ) ;
ALTER TABLE assay ADD CONSTRAINT assay_UN UNIQUE ( assay_accession ) ;

CREATE TABLE cluster_has_psm
(
  cluster_fk INTEGER NOT NULL ,
  psm_fk     INTEGER NOT NULL ,
  ratio      NUMBER (10,5) NOT NULL ,
  rank       NUMBER (10,5) NOT NULL
)
TABLESPACE PRIDECLUS_TAB LOGGING ;
CREATE INDEX psm_has_cluster_IDX ON cluster_has_psm
(
  psm_fk ASC ,
  cluster_fk ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX cluster_ratio_rank_IDX ON cluster_has_psm
(
  ratio ASC ,
  rank ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX cluster_has_psm_IDX ON cluster_has_psm
(
  cluster_fk ASC ,
  psm_fk ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
ALTER TABLE cluster_has_psm ADD CONSTRAINT cluster_has_psm_PK PRIMARY KEY ( cluster_fk, psm_fk ) ;

CREATE TABLE cluster_has_spectrum
(
  cluster_fk  INTEGER NOT NULL ,
  spectrum_fk INTEGER NOT NULL ,
  similarity  NUMBER (10,5) NOT NULL
)
TABLESPACE PRIDECLUS_TAB LOGGING ;
CREATE UNIQUE INDEX spec_has_cluster_IDX ON cluster_has_spectrum
(
  spectrum_fk ASC , cluster_fk ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX cluster_spec_sim_IDX ON cluster_has_spectrum
(
  similarity ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE UNIQUE INDEX cluster_has_spec_IDX ON cluster_has_spectrum
(
  cluster_fk ASC , spectrum_fk ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
ALTER TABLE cluster_has_spectrum ADD CONSTRAINT cluster_to_spectrum_PK PRIMARY KEY ( cluster_fk, spectrum_fk ) ;

CREATE TABLE cluster_statistics
(
  name  VARCHAR2 (2000 CHAR) NOT NULL ,
  value NUMBER (20,4)
)
TABLESPACE PRIDECLUS_TAB LOGGING ;
ALTER TABLE cluster_statistics ADD CONSTRAINT cluster_statistics_PK PRIMARY KEY ( name ) ;

CREATE TABLE psm
(
  psm_pk                     INTEGER NOT NULL ,
  spectrum_fk                INTEGER NOT NULL ,
  assay_fk                   INTEGER NOT NULL ,
  archive_psm_id             VARCHAR2 (250 CHAR) NOT NULL ,
  sequence                   VARCHAR2 (250 CHAR) NOT NULL ,
  modifications              VARCHAR2 (2000 CHAR) ,
  modifications_standardised VARCHAR2 (2000 CHAR) ,
  search_engine              VARCHAR2 (500 CHAR) ,
  search_engine_scores       VARCHAR2 (250 CHAR) ,
  search_database            VARCHAR2 (250 CHAR) ,
  protein_accession          VARCHAR2 (250 CHAR) ,
  protein_group              VARCHAR2 (250 CHAR) ,
  protein_name               VARCHAR2 (500 CHAR) ,
  start_position             SMALLINT ,
  stop_position              SMALLINT ,
  pre_amino_acid             VARCHAR2 (10 CHAR) ,
  post_amino_acid            VARCHAR2 (10 CHAR) ,
  delta_mz                   NUMBER (10,4) ,
  quantification_label       VARCHAR2 (20 CHAR)
)
TABLESPACE PRIDECLUS_TAB LOGGING ;
CREATE INDEX psm_sequence_IDX ON psm
(
  sequence ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX psm_spectrum_IDX ON psm
(
  spectrum_fk ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX psm_assay_IDX ON psm
(
  assay_fk ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX psm_sequence_mod_IDX ON psm
(
  sequence ASC ,
  modifications ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX psm_spectrum_fk_pk_seq_IDX ON psm
(
  spectrum_fk ASC ,
  psm_pk ASC ,
  sequence ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
ALTER TABLE psm ADD CONSTRAINT peptide_PK PRIMARY KEY ( psm_pk ) ;
ALTER TABLE psm ADD CONSTRAINT psm_UN UNIQUE ( archive_psm_id ) ;

CREATE TABLE spectral_library
(
  release_version         VARCHAR2 (50 CHAR) NOT NULL ,
  release_date            DATE NOT NULL ,
  taxonomy_id             INTEGER NOT NULL ,
  species_scientific_name VARCHAR2 (200 CHAR) NOT NULL ,
  species_name            VARCHAR2 (200 CHAR) NOT NULL ,
  number_of_spectra       INTEGER NOT NULL ,
  number_of_peptides      INTEGER NOT NULL ,
  file_size               INTEGER NOT NULL ,
  file_name               VARCHAR2 (200 CHAR) NOT NULL
)
TABLESPACE PRIDECLUS_TAB LOGGING ;
CREATE INDEX spectral_library_rdate_IDX ON spectral_library
(
  release_date ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX spectral_library_version_IDX ON spectral_library
(
  release_version ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
ALTER TABLE spectral_library ADD CONSTRAINT spectral_library_PK PRIMARY KEY ( release_version, taxonomy_id, file_name ) ;

CREATE TABLE spectrum
(
  spectrum_pk      INTEGER NOT NULL ,
  spectrum_ref     VARCHAR2 (500 CHAR) NOT NULL ,
  assay_fk         INTEGER NOT NULL ,
  precursor_mz     NUMBER (7,3) NOT NULL ,
  precursor_charge SMALLINT NOT NULL ,
  is_identified    CHAR (1) NOT NULL
)
TABLESPACE PRIDECLUS_TAB LOGGING ;
CREATE INDEX spectrum_ref_IDX ON spectrum
(
  spectrum_ref ASC ,
  spectrum_pk ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX spectrum_precursor_mz_IDX ON spectrum
(
  precursor_mz ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
ALTER TABLE spectrum ADD CONSTRAINT spectrum_PK PRIMARY KEY ( spectrum_pk ) ;

CREATE TABLE spectrum_cluster
(
  cluster_pk           INTEGER NOT NULL ,
  uuid                 VARCHAR2 (50 CHAR) NOT NULL ,
  avg_precursor_mz     NUMBER (10,3) NOT NULL ,
  avg_precursor_charge INTEGER NOT NULL ,
  consensus_spectrum_mz CLOB NOT NULL ,
  consensus_spectrum_intensity CLOB NOT NULL ,
  max_ratio                     NUMBER (4,3) NOT NULL ,
  number_of_spectra             INTEGER NOT NULL ,
  total_number_of_spectra       INTEGER NOT NULL ,
  number_of_psms                INTEGER NOT NULL ,
  total_number_of_psms          INTEGER NOT NULL ,
  number_of_projects            INTEGER NOT NULL ,
  total_number_of_projects      INTEGER NOT NULL ,
  number_of_species             INTEGER NOT NULL ,
  total_number_of_species       INTEGER NOT NULL ,
  number_of_modifications       INTEGER NOT NULL ,
  total_number_of_modifications INTEGER NOT NULL ,
  quality                       SMALLINT NOT NULL ,
  annotation                    VARCHAR2 (2000 CHAR)
)
TABLESPACE PRIDECLUS_TAB LOGGING ;
CREATE INDEX cluster_precursor_mz_IDX ON spectrum_cluster
(
  avg_precursor_mz ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX cluster_num_spectra_ratio_IDX ON spectrum_cluster
(
  number_of_spectra ASC ,
  max_ratio ASC ,
  number_of_projects ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
CREATE INDEX spectrum_cluster_quality_IDX ON spectrum_cluster
(
  quality ASC
)
TABLESPACE PRIDECLUS_IND LOGGING ;
ALTER TABLE spectrum_cluster ADD CONSTRAINT cluster_PK PRIMARY KEY ( cluster_pk ) ;
ALTER TABLE spectrum_cluster ADD CONSTRAINT spectrum_cluster_uuid_UN UNIQUE ( uuid ) ;

ALTER TABLE cluster_has_psm ADD CONSTRAINT cluster_has_psm_cluster_FK FOREIGN KEY ( cluster_fk ) REFERENCES spectrum_cluster ( cluster_pk ) ON
DELETE CASCADE NOT DEFERRABLE ;

ALTER TABLE cluster_has_psm ADD CONSTRAINT cluster_has_psm_psm_FK FOREIGN KEY ( psm_fk ) REFERENCES psm ( psm_pk ) ON
DELETE CASCADE NOT DEFERRABLE ;

ALTER TABLE cluster_has_spectrum ADD CONSTRAINT cluster_to_spec_spec_FK FOREIGN KEY ( spectrum_fk ) REFERENCES spectrum ( spectrum_pk ) ON
DELETE CASCADE NOT DEFERRABLE ;

ALTER TABLE cluster_has_spectrum ADD CONSTRAINT cluster_to_spectrum_cluster_FK FOREIGN KEY ( cluster_fk ) REFERENCES spectrum_cluster ( cluster_pk ) ON
DELETE CASCADE NOT DEFERRABLE ;

ALTER TABLE psm ADD CONSTRAINT psm_assay_FK FOREIGN KEY ( assay_fk ) REFERENCES assay ( assay_pk ) ON
DELETE CASCADE NOT DEFERRABLE ;

ALTER TABLE psm ADD CONSTRAINT psm_spectrum_FK FOREIGN KEY ( spectrum_fk ) REFERENCES spectrum ( spectrum_pk ) ON
DELETE CASCADE NOT DEFERRABLE ;

ALTER TABLE spectrum ADD CONSTRAINT spectrum_assay_FK FOREIGN KEY ( assay_fk ) REFERENCES assay ( assay_pk ) ON
DELETE CASCADE NOT DEFERRABLE ;

CREATE SEQUENCE assay_pk_sequence START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER assay_assay_pk_TRG BEFORE
INSERT ON assay FOR EACH ROW WHEN (NEW.assay_pk IS NULL) BEGIN :NEW.assay_pk := assay_pk_sequence.NEXTVAL;
END;
/

CREATE SEQUENCE psm_pk_sequence START WITH 1 INCREMENT BY 1000 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER psm_psm_pk_TRG BEFORE
INSERT ON psm FOR EACH ROW WHEN (NEW.psm_pk IS NULL) BEGIN :NEW.psm_pk := psm_pk_sequence.NEXTVAL;
END;
/

CREATE SEQUENCE spectrum_pk_sequence START WITH 1 INCREMENT BY 1000 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER spectrum_spectrum_pk_TRG BEFORE
INSERT ON spectrum FOR EACH ROW WHEN (NEW.spectrum_pk IS NULL) BEGIN :NEW.spectrum_pk := spectrum_pk_sequence.NEXTVAL;
END;
/

CREATE SEQUENCE cluster_pk_sequence START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER spectrum_cluster_cluster_pk BEFORE
INSERT ON spectrum_cluster FOR EACH ROW WHEN (NEW.cluster_pk IS NULL) BEGIN :NEW.cluster_pk := cluster_pk_sequence.NEXTVAL;
END;
/


-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                             8
-- CREATE INDEX                            25
-- ALTER TABLE                             18
-- CREATE VIEW                              0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           4
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          4
-- CREATE MATERIALIZED VIEW                 0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        2
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
