package mapmatching;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class MatchingSpout extends BaseRichSpout {
	private SpoutOutputCollector _collector;

	@Override
	public void nextTuple() {
		// TODO Auto-generated method stub
		String JsonContext = ReadFile("/root/mapmatching/project_jstorm/111.txt");
		JSONArray jsonArray = JSONArray.fromObject(JsonContext);
		int size = jsonArray.size();
		//System.out.println("Size: " + size);
		for(int  i = 0; i < size; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			_collector.emit(new Values(jsonObject.get("devicesn"), jsonObject.get("timestamp"), jsonObject.get("lon"), jsonObject.get("lat"), jsonObject.get("spd")));
		}
	}

	@Override
	public void open(Map arg0, TopologyContext topologyContext, SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		this._collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("devicesn", "timestamp", "lon", "lat", "spd"));
	}

	public String ReadFile(String path){
	    File file = new File(path);
	    BufferedReader reader = null;
	    String laststr = "";
	    try {
	     //System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
	     reader = new BufferedReader(new FileReader(file));
	     String tempString = null;
	     //һ�ζ���һ�У�ֱ������nullΪ�ļ�����
	     while ((tempString = reader.readLine()) != null) {
	      //��ʾ�к�
	      laststr = laststr + tempString + ','; //���ڹ켣�ļ���ʽ���淶����Ҫ�������϶��Ų�����ȷʶ��Ϊjson��ʽ
	     }
	     reader.close();
	    } catch (IOException e) {
	     e.printStackTrace();
	    } finally {
	     if (reader != null) {
	      try {
	       reader.close();
	      } catch (IOException e1) {
	      }
	     }
	    }
	    return laststr;
	}
}
