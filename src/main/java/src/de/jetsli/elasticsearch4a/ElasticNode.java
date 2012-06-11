package de.jetsli.elasticsearch4a;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

/**
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class ElasticNode {

    private static Logger logger = Logger.getLogger(ElasticNode.class);
    public static final String CLUSTER = "jetwickcluster";
    public static final int PORT = 9300;

    public static void main(String[] args) throws IOException, InterruptedException {
        ElasticNode node = new ElasticNode().start("es");
        node.waitForYellow();
        logger.info(node.getInfo());
    }
    private Node node;
    private boolean started = false;

    public ElasticNode start(String dataHome) {
        return start(dataHome, dataHome + "/config", false);
    }

    public ElasticNode start(String home, String conf, boolean testing) {
        // see
        // http://www.elasticsearch.com/docs/elasticsearch/setup/installation/
        // http://www.elasticsearch.com/docs/elasticsearch/setup/dirlayout/
        File homeDir = new File(home);
        System.setProperty("es.path.home", homeDir.getAbsolutePath());
        System.setProperty("es.path.conf", conf);

        Builder settings = ImmutableSettings.settingsBuilder();

        if (testing) {
            settings.put("gateway.type", "none");
            // default is local
            // none means no data after node restart!
            // does not work when transportclient connects:
//                put("gateway.type", "fs").
//                put("gateway.fs.location", homeDir.getAbsolutePath()).
        }

        settings.build();
        NodeBuilder nBuilder = nodeBuilder().settings(settings);
        if (!testing) {
            nBuilder.clusterName(CLUSTER);
        } else {
            nBuilder.local(true);
        }

        node = nBuilder.build().start();
        started = true;
        logger.info("Started Node in cluster " + CLUSTER + ". Home folder: " + homeDir.getAbsolutePath());
        return this;
    }

    public void stop() {
        if (node == null)
            throw new RuntimeException("Node not started");

        started = false;
        node.close();
    }

    public boolean isStarted() {
        return started;
    }

    public Client client() {
        if (node == null)
            throw new RuntimeException("Node not started");

        return node.client();
    }

    /**
     * Warning: Can take several seconds!
     */
    public void waitForYellow() {
        node.client().admin().cluster().health(new ClusterHealthRequest("twindex").waitForYellowStatus()).actionGet();
        logger.info("Now node status is 'yellow'!");
    }

    public void waitForOneActiveShard() {
        node.client().admin().cluster().health(new ClusterHealthRequest("twindex").waitForActiveShards(1)).actionGet();
        logger.info("Now node has at least one active shard!");
    }

    public String getInfo() {
        NodesInfoResponse rsp = node.client().admin().cluster().nodesInfo(new NodesInfoRequest()).actionGet();
        String str = "Cluster:" + rsp.getClusterName() + ". Active nodes:";
        return str + rsp.getNodesMap().keySet();        
    }
}
