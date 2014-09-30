package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class AssaySummary {
    private Long id;
    private String accession;
    private String projectAccession;
    private String projectTitle;
    private String assayTitle;
    private String species;
    private boolean multiSpecies;
    private String taxonomyId;
    private String disease;
    private String tissue;
    private String searchEngine;
    private String instrument;
    private String instrumentType;
    private boolean bioMedical;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getProjectAccession() {
        return projectAccession;
    }

    public void setProjectAccession(String projectAccession) {
        this.projectAccession = projectAccession;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getAssayTitle() {
        return assayTitle;
    }

    public void setAssayTitle(String assayTitle) {
        this.assayTitle = assayTitle;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public boolean isMultiSpecies() {
        return multiSpecies;
    }

    public void setMultiSpecies(boolean multiSpecies) {
        this.multiSpecies = multiSpecies;
    }

    public String getTaxonomyId() {
        return taxonomyId;
    }

    public void setTaxonomyId(String taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getTissue() {
        return tissue;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public boolean isBioMedical() {
        return bioMedical;
    }

    public void setBioMedical(boolean bioMedical) {
        this.bioMedical = bioMedical;
    }
}
