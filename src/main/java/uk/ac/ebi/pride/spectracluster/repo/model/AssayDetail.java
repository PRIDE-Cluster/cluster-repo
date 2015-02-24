package uk.ac.ebi.pride.spectracluster.repo.model;


import uk.ac.ebi.pride.spectracluster.repo.utils.StringUtils;

import java.util.Set;

/**
 * Details of an assay
 *
 * @author Rui Wang
 * @version $Id$
 */
public class AssayDetail {
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

    public Set<String> getSpeciesEntries() {
        return StringUtils.split(species, ",");
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

    public Set<String> getTaxonomyIdEntries() {
        return StringUtils.split(taxonomyId, ",");
    }

    public void setTaxonomyId(String taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    public String getDisease() {
        return disease;
    }

    public Set<String> getDiseaseEntries() {
        return StringUtils.split(disease, ",");
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getTissue() {
        return tissue;
    }

    public Set<String> getTissueEntries() {
        return StringUtils.split(tissue, ",");
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public Set<String> getSearchEngineEntries() {
        return StringUtils.split(searchEngine, ",");
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getInstrument() {
        return instrument;
    }

    public Set<String> getInstrumentEntries() {
        return StringUtils.split(instrument, ",");
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public Set<String> getInstrumentTypeEntries() {
        return StringUtils.split(instrumentType, ",");
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

    @Override
    public String toString() {
        return "AssayDetail{" +
                "id=" + id +
                ", accession='" + accession + '\'' +
                ", projectAccession='" + projectAccession + '\'' +
                ", projectTitle='" + projectTitle + '\'' +
                ", assayTitle='" + assayTitle + '\'' +
                ", species='" + species + '\'' +
                ", multiSpecies=" + multiSpecies +
                ", taxonomyId='" + taxonomyId + '\'' +
                ", disease='" + disease + '\'' +
                ", tissue='" + tissue + '\'' +
                ", searchEngine='" + searchEngine + '\'' +
                ", instrument='" + instrument + '\'' +
                ", instrumentType='" + instrumentType + '\'' +
                ", bioMedical=" + bioMedical +
                '}';
    }
}
