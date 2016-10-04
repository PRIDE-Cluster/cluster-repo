package uk.ac.ebi.pride.spectracluster.repo.model;

import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.spectracluster.repo.utils.ModificationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class helps other tools to consume Clustered PSMs including the information of the PSMs, assays and cluster where they belongs.
 * Specially this object has been used by the cluster-file-exporter. This object can be use for aggregate peptides.
 *
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 29/02/2016
 */
public class ClusteredPSMReport implements Comparable{

    private java.lang.Long clusterId;                            // internal cluster id

    private String sequence;                          // peptide sequence
    private List<ModificationProvider> modifications; // modifications as provider

    private float psmRatio;                           // ratio is the number of distinct psm / the number of spectra
    private float rank;                               // rank of the psm by ratio
    private java.lang.Long psmId;                               // internal psm id

    private String proteinAccession;        // Protein Accession for the current PSMs

    private float clusterMaxPeptideRatio;

    private int clusterNumberProjects;

    private ClusterQuality quality;

    private int numberOfSpectra;

    private long assayID;

    private float deltaMZ;

    private float peptideRatio;

    private int clusterNumberPSMs;

    private int clusterNumberSpectra;

    private float clusterAvgMz;

    private float clusterAvgCharge;

    private AssayReport assay;

    private List<String> wrongAnnotations = new ArrayList<String>();

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public List<ModificationProvider> getModifications() {
        return modifications;
    }

    public void setModifications(List<ModificationProvider> modifications) {
        this.modifications = modifications;
    }

    public void setModifications(String modifications){
        this.modifications = ModificationUtils.getModifications(modifications);
    }

    public float getPsmRatio() {
        return psmRatio;
    }

    public void setPsmRatio(float psmRatio) {
        this.psmRatio = psmRatio;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public java.lang.Long getPsmId() {
        return psmId;
    }

    public void setPsmId(java.lang.Long psmId) {
        this.psmId = psmId;
    }

    public String getProteinAccession() {
        return proteinAccession;
    }

    public void setProteinAccession(String proteinAccession) {
        this.proteinAccession = proteinAccession;
    }

    public float getClusterMaxPeptideRatio() {
        return clusterMaxPeptideRatio;
    }

    public void setClusterMaxPeptideRatio(float clusterMaxPeptideRatio) {
        this.clusterMaxPeptideRatio = clusterMaxPeptideRatio;
    }

    public int getClusterNumberProjects() {
        return clusterNumberProjects;
    }

    public void setClusterNumberProjects(int clusterNumberProjects) {
        this.clusterNumberProjects = clusterNumberProjects;
    }

    public ClusterQuality getQuality() {
        return quality;
    }

    public void setQuality(ClusterQuality quality) {
        this.quality = quality;
    }

    public void setNumberOfSpectra(int numberOfSpectra) {
        this.numberOfSpectra = numberOfSpectra;
    }

    public int getNumberOfSpectra() {
        return numberOfSpectra;
    }

    public void setAssayID(long assayID) {
        this.assayID = assayID;
    }

    public long getAssayID() {
        return assayID;
    }

    public void setDeltaMZ(float deltaMZ) {
        this.deltaMZ = deltaMZ;
    }

    public float getDeltaMZ() {
        return deltaMZ;
    }

    public void setPeptideRatio(float peptideRatio) {
        this.peptideRatio = peptideRatio;
    }

    public float getPeptideRatio() {
        return peptideRatio;
    }

    public void setClusterNumberPSMs(int clusterNumberPSMs) {
        this.clusterNumberPSMs = clusterNumberPSMs;
    }

    public int getClusterNumberPSMs() {
        return clusterNumberPSMs;
    }

    public void setClusterNumberSpectra(int clusterNumberSpectra) {
        this.clusterNumberSpectra = clusterNumberSpectra;
    }

    public int getClusterNumberSpectra() {
        return clusterNumberSpectra;
    }

    public void setClusterAvgMz(float clusterAvgMz) {
        this.clusterAvgMz = clusterAvgMz;
    }

    public float getClusterAvgMz() {
        return clusterAvgMz;
    }

    public void setClusterAvgCharge(float clusterAvgCharge) {
        this.clusterAvgCharge = clusterAvgCharge;
    }

    public float getClusterAvgCharge() {
        return clusterAvgCharge;
    }

    public PeptideForm getPeptideForm() {
        return new PeptideForm(this.getSequence(), this.getModifications());
    }

    public AssayReport getAssay() {
        return assay;
    }

    public void setAssay(AssayReport assay) {
        this.assay = assay;
    }

    public boolean isWrongAnnotated(){
        return !wrongAnnotations.isEmpty();
    }

    public void addWrongAnnotation(String ptmWrongAnnotated) {
        wrongAnnotations.add(ptmWrongAnnotated);
    }

    @Override
    public int compareTo(Object o) {
        if(clusterId != ((ClusteredPSMReport) o).clusterId)
            clusterId.compareTo(((ClusteredPSMReport) o).clusterId);
        return getPeptideForm().compareTo(((ClusteredPSMReport)o).getPeptideForm());
    }


}
