package mapmatching;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.SpoutDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.alibaba.jstorm.utils.JStormUtils;

public class MatchingTopology {
	private static Map conf = new HashMap<Object, Object>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TopologyBuilder builder = new TopologyBuilder();
		//����topology��������

		int spoutParal = JStormUtils.parseInt(conf.get("spout.parallel"), 1);
		//��ȡspout�Ĳ�������

		SpoutDeclarer spout = builder.setSpout("MatchingSpout", new MatchingSpout(), spoutParal);
		//����Spout�� ����new SequenceSpout() Ϊ����spout����SequenceTopologyDef.SEQUENCE_SPOUT_NAME Ϊspout�����֣�ע�������в�Ҫ���пո�

		int boltParal = JStormUtils.parseInt(conf.get("bolt.parallel"), 1);
		//��ȡbolt�Ĳ�������

		BoltDeclarer totalBolt = builder.setBolt("MatchingBolt", new MatchingBolt(), boltParal).fieldsGrouping("MatchingSpout", new Fields("devicesn"));
		//����bolt�� SequenceTopologyDef.TOTAL_BOLT_NAME Ϊbolt���֣�TotalCount Ϊbolt����boltParalΪbolt��������
		//shuffleGrouping��SequenceTopologyDef.SEQUENCE_SPOUT_NAME���� 
		//��ʾ����SequenceTopologyDef.SEQUENCE_SPOUT_NAME�����ݣ�������shuffle��ʽ��
		//��ÿ��spout�����ѯ����tuple����һ��bolt��

		int ackerParal = JStormUtils.parseInt(conf.get("acker.parallel"), 1);
		Config.setNumAckers(conf, ackerParal);
		//���ñ�ʾacker�Ĳ�����

		int workerNum = JStormUtils.parseInt(conf.get("worker.num"), 10);
		Config.setNumWorkers(conf, workerNum);
		//��ʾ����topology��ʹ�ü���worker

		conf.put(Config.NIMBUS_HOST, "localhost");

		try {
			StormSubmitter.submitTopology("MatchingTopology", conf, builder.createTopology());
		} catch (AlreadyAliveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//�ύtopology

	}

}
