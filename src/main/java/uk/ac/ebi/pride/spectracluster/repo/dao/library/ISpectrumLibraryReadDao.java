package uk.ac.ebi.pride.spectracluster.repo.dao.library;

import uk.ac.ebi.pride.spectracluster.repo.model.SpectrumLibraryDetail;

import java.util.List;

/**
 * Read interface for spectral library details
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface ISpectrumLibraryReadDao {

    /**
     * Get the version of the latest spectral library release
     * @return  the latest spectral library version
     */
    String getLatestSpectrumLibraryVersion();

    /**
     * Get the spectral libraries belong to the latest release
     * @return  a list of spectral library details
     */
    List<SpectrumLibraryDetail> getLatestSpectrumLibraries();

    /**
     * Get all the spectral libraries relate to all releases
     * @return  all spectral library details
     */
    List<SpectrumLibraryDetail> getAllSpectrumLibraries();
}
