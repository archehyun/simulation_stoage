package sim.view;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sim.model.core.IFSimModel;
import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.impl.CrossATC2;
import sim.model.impl.stoage.atc.impl.CrossLandSideATC;
import sim.model.impl.stoage.atc.impl.CrossSeaSideATC;
import sim.model.impl.stoage.block.Block;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.commom.JobManager;
import sim.model.impl.stoage.commom.UnparserableCommandException;
import sim.model.impl.stoage.manager.ATCJobManager;
import sim.model.impl.stoage.manager.ATCManager;
import sim.view.framework.SimCanvas;

/**
 *
 * 장치장 ATC 타입에 따른 성능을 시뮬레이션 하는 프로그램
 * a simulation to merasure the  prefomance of Automatic Transfer Crane  type
 *
 * 3가지 타입의 atc 가 있다
 * there is three type of atc
 *
 * 첫번째는 크로스 타입니다.
 * the first is Cross type,  it has two Crane
 *
 * @author  ARCHEHYUN
 *
 */
public class SimMain {

	SimCanvas canvas;

	List<IFSimModel> list = new LinkedList<IFSimModel>();

	public void render(double delta) {

		Iterator<IFSimModel> iter = list.iterator();
		while (iter.hasNext()) {
			iter.next().update(delta);
		}

	}

	public void setCanvas(SimCanvas canvas) {
		this.canvas = canvas;
	}

	ATCManager atcManagerImpl = ATCManager.getInstance();

	ATCJobManager atcManager;


	JobManager jobManager = JobManager.getInstance();

	private Element element;

	static BlockManager blockManager = BlockManager.getInstance();

	public void parse() throws SAXException, IOException, URISyntaxException, ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document xml = null;

		xml = documentBuilder.parse(ClassLoader.getSystemResource("layout/layout.xml").toURI().toString());

		element = xml.getDocumentElement();

	}

	public void createInit()
	{
		try {
			NodeList blockList = element.getElementsByTagName("block");

			blockManager.block = blockList.getLength();

			blockManager.blockInit();

			if (blockList.getLength() > 0) {
				//반복문 이용
				for (int i = 0; i < blockList.getLength(); i++) {
					//blog xml태그의 자식태그 한번 더 획득
					Element blockElement = (Element) blockList.item(i);

					String type = blockElement.getAttribute("type");
					String use = blockElement.getAttribute("use");

					if (!Boolean.valueOf(use))
						continue;

					int blockID = createBlock(blockElement);

					atcManager = atcManagerImpl.createManger(blockID, type);

					NodeList atcList = blockElement.getElementsByTagName("atc");


					if (atcList.getLength() > 0) {
						for (int j = 0; j < atcList.getLength(); j++) {
							Node atcNode = atcList.item(j);

							createATC(blockID, atcManager, atcNode);
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createATC(int blockID, ATCJobManager atcManager, Node atcNode) {

		Element atcElement = (Element) atcNode;
		String type = atcElement.getAttribute("type");
		atcElement.getAttribute("id");
		atcElement.getAttribute("x");
		atcElement.getAttribute("y");
		NodeList initLocations = atcElement.getElementsByTagName("initLcation");
		Element initLocation = (Element) initLocations.item(0);

		SimATC atc = null;
		if (atcManager.getType().equals("cross")) {
			if (type.equals("sea")) {
				atc = new CrossSeaSideATC("atc_sea-" + blockID, blockID + SimATC.SEA_SIDE, blockID, 0, 0, BlockManager.conW * BlockManager.ROW + 4, BlockManager.conH, SimATC.TYPE_SEA);
			} else if (type.equals("land")) {
				atc = new CrossLandSideATC("atc_land-" + blockID, blockID + SimATC.LAND_SIDE, blockID, 0, 25, BlockManager.conW * BlockManager.ROW + 4, BlockManager.conH, SimATC.TYPE_LAND);
			}

			else if (type.equals("new_sea")) {
				atc = new CrossATC2("atc_sea-" + blockID, blockID + SimATC.SEA_SIDE, blockID, 0, 0, BlockManager.conW * BlockManager.ROW + 4, BlockManager.conH, SimATC.TYPE_SEA);
			}
			else if (type.equals("new_land")) {
				atc = new CrossATC2("atc_land-" + blockID, blockID + SimATC.LAND_SIDE, blockID, 0, 25, BlockManager.conW * BlockManager.ROW + 4, BlockManager.conH, SimATC.TYPE_LAND);
			}
			else {
				System.out.println("type:" + type);
			}

			list.add(atc);
		}
		atc.setInitBlockLocation(Integer.parseInt(initLocation.getAttribute("row")), Integer.parseInt(initLocation.getAttribute("bay")));
		atc.setSpeed(ATCJobManager.SPEED);
		atcManager.addSimModel(atc);
		canvas.addObject(atc);

	}

	private int createBlock(Element node) {

		Element blockElement = node;
		blockElement.getNodeName();
		blockElement.getAttribute("id");
		int blockID = Integer.parseInt(blockElement.getAttribute("id"));
		int x = Integer.parseInt(blockElement.getAttribute("x"));
		int y = Integer.parseInt(blockElement.getAttribute("y"));

		Block blocks = blockManager.getBlock(blockID);

		this.list.add(blocks);
		blocks.setLocation(x, y);
		canvas.addObject(blocks);

		return blockID;
	}

	/**
	 * @throws ParserConfigurationException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SAXException
	 *
	 */
	public void init()
	{
		createInit();

	}

	/**
	 *start simulation
	 *init block view, atc viw
	 */
	public void simulationStart()
	{
		atcManagerImpl.simStart();
		canvas.start();
	}

	/**
	 * simulation stop
	 */
	public void simulationStop()
	{
		SimEvent event = new SimEvent(0);
		event.setEventMessage("simstop");
		jobManager.append(event);
		atcManagerImpl.simStop();
	}

	public SimMain() {
		try {
			parse();
		} catch (SAXException | IOException | URISyntaxException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void clear() {
		jobManager.clear();
		blockManager.clear();
		canvas.clear();
	}


	public void blockInit() {

		blockManager.blockInit();
		SimEvent event = new SimEvent();
		event.add("type", "block");
		blockManager.notifyMonitor(event);
	}

	public void putOrder() {
		jobManager.putOrder();

	}

	public void append(SimEvent event) {
		jobManager.append(event);

	}

	public void putCommand(String command) throws ArrayIndexOutOfBoundsException, UnparserableCommandException {
		jobManager.putCommand(command);
	}

	public Node getRoot() {
		// TODO Auto-generated method stub
		return element;
	}

	public void updateATCSpeed(float speed) {
		SimEvent event = new SimEvent(0, SimEvent.TYPE_COMMAND);
		event.setCommandType(SimEvent.COMMAND_UPDATE_SPEED);

		event.add("speed", speed);

		ATCJobManager.SPEED = speed;
		atcManagerImpl.append(event);



	}

}
