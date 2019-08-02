package chouc.storm.count;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class WordCountTopology {

    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("mySpout",new MySpout(),1);
        topologyBuilder.setBolt("mySplitBolt",new MySplitBolt(),1).shuffleGrouping("mySpout");
        topologyBuilder.setBolt("myCountBolt",new MyCountBolt(),2).fieldsGrouping("mySplitBolt", new Fields("word"));

        Config config = new Config();
        /*config.setDebug(true);*/
        config.setNumWorkers(1);

        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("mywordcount",config,topologyBuilder.createTopology());



    }
}
