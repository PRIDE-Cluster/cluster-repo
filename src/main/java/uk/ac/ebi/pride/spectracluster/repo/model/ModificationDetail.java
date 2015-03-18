package uk.ac.ebi.pride.spectracluster.repo.model;

import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.archive.dataprovider.param.CvParamProvider;

import java.util.Map;

/**
 * Details of a post translational modification
 *
 * @author Rui Wang
 * @version $Id$
 */
/* TODO Add support for Neutral Losses and Position Map */
public class ModificationDetail implements ModificationProvider {
    private String accession;
    private String name;
    private int position;

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getName() {
        return name;
    }

    @Override
    public Integer getMainPosition() {
        return getPosition();
    }

    @Override
    public CvParamProvider getNeutralLoss() {
        return null;
    }

    @Override
    public Map<Integer, CvParamProvider> getPositionMap() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModificationDetail)) return false;

        ModificationDetail modificationDetail = (ModificationDetail) o;

        if (position != modificationDetail.position) return false;
        if (accession != null ? !accession.equals(modificationDetail.accession) : modificationDetail.accession != null) return false;
        if (name != null ? !name.equals(modificationDetail.name) : modificationDetail.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accession != null ? accession.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + position;
        return result;
    }

    @Override
    public String toString() {
        return "PTMDetail{" +
                "accession='" + accession + '\'' +
                ", name='" + name + '\'' +
                ", position=" + position +
                '}';
    }
}
