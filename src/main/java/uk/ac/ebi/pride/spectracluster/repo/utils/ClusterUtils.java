package uk.ac.ebi.pride.spectracluster.repo.utils;

import uk.ac.ebi.pride.spectracluster.repo.model.ClusterDetail;
import uk.ac.ebi.pride.spectracluster.repo.model.ClusteredPSMDetail;

import java.util.*;

/**
 * Utility methods for aiding cluster related computation
 *
 * @author Rui Wang
 * @version $Id$
 */
public final class ClusterUtils {
    private static final ClusteredPSMRatioComparator comparator = new ClusteredPSMRatioComparator();

    private ClusterUtils() {}

    /**
     * Update statistics on the clustered PSMs
     *
     * @param cluster cluster
     */
    public static void updateClusteredPSMStatistics(final ClusterDetail cluster) {
        float size = (float) cluster.getClusteredSpectrumDetails().size();

        List<ClusteredPSMDetail> psmDetails = cluster.getClusteredPSMDetails();
        // this map is used to store the ranking of a psm within a group of psms sharing the same peptide sequence
        Map<ClusteredPSMDetail, Integer> groupedPsmDetails = new HashMap<ClusteredPSMDetail, Integer>();
        for (ClusteredPSMDetail clusteredPSMDetail : psmDetails) {
            String sequence = clusteredPSMDetail.getSequence();
            List<ClusteredPSMDetail> clusteredPSMDetails = cluster.getClusteredPSMDetails(sequence);

            // calculate ranking within the sequence group
            Map<ClusteredPSMDetail, Integer> groupRanking = calculateGroupRanking(clusteredPSMDetails);
            groupedPsmDetails.putAll(groupRanking);

            int count = countDistinctSpectra(clusteredPSMDetails);
            float ratio = count * 1.0f / size;
            clusteredPSMDetail.setPsmRatio(ratio);
        }

        Collections.sort(psmDetails, comparator);

        int rank = 0;
        float currentRatio = -1;
        for (ClusteredPSMDetail psmSummary : psmDetails) {
            float psmRatio = psmSummary.getPsmRatio();
            if (psmRatio != currentRatio) {
                currentRatio = psmRatio;
                rank++;
            }
            Integer groupRank = groupedPsmDetails.get(psmSummary);
            psmSummary.setRank(Float.parseFloat(rank + "." + groupRank));
        }
    }

    public static Map<ClusteredPSMDetail, Integer> calculateGroupRanking(List<ClusteredPSMDetail> clusteredPSMDetails) {
        Map<ClusteredPSMDetail, Integer> groupRanking = new HashMap<ClusteredPSMDetail, Integer>();

        Map<String, List<ClusteredPSMDetail>> groupBySeqAndMod = new HashMap<String, List<ClusteredPSMDetail>>();

        // group psm details according to their sequence and modifications first
        for (ClusteredPSMDetail clusteredPSMSummary : clusteredPSMDetails) {
            String seqAndMod = clusteredPSMSummary.getSequence() + clusteredPSMSummary.getModifications();

            List<ClusteredPSMDetail> groupedPSMDetails = groupBySeqAndMod.get(seqAndMod);
            if (groupedPSMDetails == null) {
                groupedPSMDetails = new ArrayList<ClusteredPSMDetail>();
                groupBySeqAndMod.put(seqAndMod, groupedPSMDetails);
            }

            groupedPSMDetails.add(clusteredPSMSummary);
        }

        // sort the group
        List<List<ClusteredPSMDetail>> groups = new ArrayList<List<ClusteredPSMDetail>>(groupBySeqAndMod.values());
        Collections.sort(groups, new Comparator<List<ClusteredPSMDetail>>() {
            @Override
            public int compare(List<ClusteredPSMDetail> group1, List<ClusteredPSMDetail> group2) {
                return group2.size() - group1.size();
            }
        });

        // apply ranking
        int rank = 0;
        int currentSize = 0;
        for (List<ClusteredPSMDetail> group : groups) {
            if (group.size() != currentSize) {
                currentSize = group.size();
                rank++;
            }

            for (ClusteredPSMDetail clusteredPSMDetail : group) {
                groupRanking.put(clusteredPSMDetail, rank);
            }
        }

        return groupRanking;
    }

    private static int countDistinctSpectra(List<ClusteredPSMDetail> psms) {
        Set<String> psmRepresentations = new HashSet<String>();

        for (ClusteredPSMDetail psm : psms) {
            psmRepresentations.add(psm.getSpectrumId() + "");
        }

        return psmRepresentations.size();
    }


    private static class ClusteredPSMRatioComparator implements Comparator<ClusteredPSMDetail> {

        @Override
        public int compare(ClusteredPSMDetail o1, ClusteredPSMDetail o2) {
            return -(Float.compare(o1.getPsmRatio(), o2.getPsmRatio()));
        }
    }
}
