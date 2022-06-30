package riverport;

import java.util.HashMap;
import java.util.Map;


import process.Dispatcher;
import process.MultiActor;
import process.QueueForTransactions;
import process.Store;
import stat.DiscretHisto;
import stat.Histo;
import stat.IHisto;
import widgets.experiments.IExperimentable;
import widgets.stat.IStatisticsable;
import widgets.trans.ITransMonitoring;
import widgets.trans.ITransProcesable;

/**
 * Клас моделі Модель складається з БУЛЬДОЗЕРА, НАВАНТАЖУВАЧА та кількох
 * САМОСКИДІВ. Бульдозер згрібає ґрунт у КУПУ. На одну порцію ґрунту
 * він витрачає випадковий час. Навантажувач починає працювати коли у купі
 * є ґрунт і в черзі є самоскид, що чекає завантаження. Навантажувач
 * порціями насипає ґрунт з купи у кузов самоскиду. Місткість кузову 
 * самоскида - декілька порцій ґрунту.  Самоскиди виїжджають із автопарку 
 * і стають у ЧЕРГУ до навантажувача. Після завантаження самоскиди везуть 
 * ґрунт замовнику, розвантажуються, і повертаються до навантажувача.
 */
public class RiverModel implements IExperimentable, ITransProcesable,
		IStatisticsable { 
	
	// Посилання на диспетчера
	private Dispatcher dispatcher;

	// Посилання на візуальну частину
	private RiverPortGUI gui;

	// Баржа
	private Barge barge;

	// Погрузчики
	private Loader loader;
	
	// Авто
	private Cars cars;

	// Бригада Авто
	private MultiActor multiAuto;

	private MultiActor multiLoader;
	
	private MultiActor multiBarge;
	
	//платформа
	private Store platform;
	
	//Черга барж на розвантаження
	private QueueForTransactions<Barge> Barge_queueToLoader;
	
	// Черга авто до навантажувача
	private QueueForTransactions<Cars> queueToLoader;

	// Уявна черга для авто у дорозі
	private QueueForTransactions<Cars> queueLorryOnRoad;
		
	/////////////////////////////////////////////////////////////
	//Накопичувачі статистики створюємо одразу з пакетним доступом
	/////////////////////////////////////////////////////////////
	// Гістограма для довжини черги авто до погрузчиків
	DiscretHisto histoForQueueToLoader = new DiscretHisto();

	// Гістограма для довжини черги барж до погрузчиків
	DiscretHisto histoForBargeQueueToLoader = new DiscretHisto();
	
	// Гістограма для часу простою погрузчиків
	Histo histoLoader = new Histo();
	
	// Гістограма для часу простою баржі
	Histo histoBarge = new Histo();

	// Гістограма для часу простою авто
	Histo histoAuto = new Histo();

	// Гістограма для кількості контейнерів що знаходяться на платформі
	Histo histoPlatform = new Histo();

	// ////////////////////////////////////////
	// Єдиний спосіб створити модель, це викликати цей конструктор
	// Він гарантовано передає посилання на диспетчера та GUI
	// ////////////////////////////////////////

	public RiverModel(Dispatcher d, RiverPortGUI g) {
		if (d == null || g == null) {
			System.out.println("Не задано диспетчера або GUI для моделі");
			System.out.println("Подальша робота неможлива");
			System.exit(0);
		}
		dispatcher = d;
		gui = g;
		// Передаємо акторів до стартового списку диспетчера
		componentsToStartList();
	}

	// Передача акторів диспетчеру
	private void componentsToStartList() {
		dispatcher.addStartingActor(getMultiBarge());
		dispatcher.addStartingActor(getMultiAuto());
		dispatcher.addStartingActor(getMultiLoaders());
	}

	// ////////////////////////////////////////
	// Методи відкладеного створення акторів моделі,
	// ////////////////////////////////////////

	
	public Histo gethistoPlatform() {
		return histoPlatform;
	}
	
	public Dispatcher getDispatcher() {
		
		return dispatcher;
	}
	
	// Meтод створення баржі
	public Barge getBarge() {
		if (barge == null) {
			barge = new Barge("Баржа", gui, this);
		}
		return barge;
	}

	
	public MultiActor getMultiBarge() {
		if(multiBarge == null) {
			multiBarge = new MultiActor("MultiActor для барж",
					getBarge(), gui.getChooseDataBargeNumber().getInt());
		}
		return multiBarge;
	}
	
	// Meтод створення погрузчика
	public Loader getLoader() {
		if (loader == null)
			loader = new Loader("Погрузчики", gui, this);
		return loader;
	}
	
	public MultiActor getMultiLoaders() {
		if(multiLoader == null) {
			multiLoader = new MultiActor("MultiActor для погрузчиків",
					getLoader(), gui.getChooseDataLoaderNumber().getInt());
		}
		return multiLoader;
	}
	
	// Meтод створення зразка авто
	public Cars getAuto() {
		if (cars == null)
			cars = new Cars("Авто", gui, this);
		return cars;
	}

	//Meтод створення групи авто
	public MultiActor getMultiAuto() {
		if (multiAuto == null) {
			multiAuto = new MultiActor("MultiActor для авто",
					getAuto(), gui.getChooseDataCarsNumber().getInt());
		}
		return multiAuto;
	}


	public Store getPlatform() {
		if (platform == null) {
			platform = new Store("Кількість контейнерів", dispatcher);
		}
		return platform;
	}
	
	// Meтод створення черги авто до погрузчиків
	public QueueForTransactions<Cars> getQueueToLoader() {
		if (queueToLoader == null) {
			queueToLoader = new QueueForTransactions<Cars>(
					"Черга авто до погрузчиків", dispatcher, 
					histoForQueueToLoader);
		}
		return queueToLoader;
	}
	
	// Meтод створення черги барж до погрузчиків
	public QueueForTransactions<Barge> getBargeQueueToLoader() {
		if (Barge_queueToLoader == null) {
			Barge_queueToLoader = new QueueForTransactions<Barge>(
					"Черга барж до погрузчиків", dispatcher, 
					histoForBargeQueueToLoader);
		}
		return Barge_queueToLoader;
	}
	
	
	// Meтод створення уявної черги "авто у дорозі"
	public QueueForTransactions<Cars> getQueueLorryOnRoad() {
		if (queueLorryOnRoad == null) {
			queueLorryOnRoad = new QueueForTransactions<Cars>(
					"Авто, що у дорозі", dispatcher);
		}
		return queueLorryOnRoad;
	}

	// ///////////////////////////////////////////////////////////
	// Методи ініціалізації моделі та реалізація інтерфейсів
	// //////////////////////////////////////////////////////////////

	// Ініціалізація для режиму "Тест"
	public void initForTest() {
		// Передаємо чергам painter-ів для динамічної індикації
		getBargeQueueToLoader().setPainter(gui.getDiagramBarge().getPainter());
		getQueueToLoader().setPainter(gui.getDiagramCars().getPainter());
		getQueueLorryOnRoad().setPainter(gui.getDiagramCarsonRoad().getPainter());
		if (gui.getJCheckBox().isSelected())
			dispatcher.setProtocolFileName("Console");
	}

	
	// /////////////////////////////////////
	// Реалізація інтерфейсу IStatisticsable
	// /////////////////////////////////////
	
	// Ініціалізацію для режиму "Статистика" не використовуємо
	@Override
	public void initForStatistics() {

	}
	
	//Метод формування мапи результатів
	@Override
	public Map<String, IHisto> getStatistics() {
		Map<String, IHisto> map = new HashMap<>();
		map.put("Гістограма для довжини черги до погрузчика",
				histoForQueueToLoader);
		map.put("Гістограма для часу простою авто", histoAuto);
		map.put("Гістограма для часу простою погрузчиків", histoLoader);
		map.put("Гістограма для часу простою баржі", histoBarge);
		map.put("Гістограма кількості контейнерів що знаходяться на платформі", histoPlatform);
		return map;
	}

	// /////////////////////////////////////
	// Реалізація інтерфейсу IExperimentable
	// /////////////////////////////////////
	
	// Налаштування для проведення експерименту
	public void initForExperiment(double factor) {
		multiBarge.setNumberOfClones((int) factor);
	}

	//Метод формування мапи результатів
	public Map<String, Double> getResultOfExperiment() {
		Map<String, Double> resultMap = new HashMap<>();
		resultMap.put("Час простою авто від кількості барж", histoAuto
				.getAverage());
		resultMap.put("Кількість контейнерів на платформі", histoPlatform.average());
		resultMap.put("Час простою барж ",
				histoBarge.getAverage());
		resultMap.put("Час простою навантажувача від кількості барж",
				histoLoader.getAverage());
		return resultMap;
	}

	// /////////////////////////////////////
	// Реалізація інтерфейсу ITransprocesable
	// /////////////////////////////////////
	
	// Налаштування перед початком моделювання
	public void initForTrans(double finishTime) {
		getBarge().setFinishTime(finishTime);
		getLoader().setFinishTime(finishTime);
		getAuto().setFinishTime(finishTime);
		gui.getChooseDataFinishTime().setDouble(finishTime);

	}

	//Метод формування мапи результатів
	@Override
	public Map<String, ITransMonitoring> getMonitoringObjects() {
		Map<String, ITransMonitoring> transMap = new HashMap<>();
		//transMap.put("Купа грунту", getContainer());
		transMap.put("Черга авто до погрузчиків", getQueueToLoader());
		transMap.put("Черга барж до погрузчиків", getBargeQueueToLoader());
		transMap.put("Авто в дорозі", getQueueLorryOnRoad());
		transMap.put("Кількість контейнерів на платформі", getPlatform());
		
		return transMap;
	}
}

