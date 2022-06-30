package riverport;

import java.util.function.BooleanSupplier;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import process.Store;
import rnd.Randomable;

/**
 * Клас для навантажувача. Абстракція «навантажувач» моделює робочу машину, що
 * навантажує ґрунт у самоскиди, беручи його з купи. Будемо вважати, що розмір
 * порції, яку навантажувач бере з купи за один раз дорівнює одинці. На
 * завантаження порції ґрунту навантажувач витрачає деякий час, що є випадковою
 * величиною. Найбільш вірогідно, що ця величина підпорядковується нормальному
 * закону. Навантажувач може працювати коли є ґрунт у купі і самоскид, що чекає
 * завантаження. Коли ж ці умови не виконуються, навантажувач чекає на їх
 * виконання. Абстракція «навантажувач» у системі безпосередньо пов’язана з
 * абстракцією «купа ґрунту» та абстракцією «черга самоскидів».
 *
 *
 */
public class Loader extends Actor {

	/**
		Кількість контейнерів
	 */
	private Store container;

	
	/**
		Місткість платформи
	 */
	private Store platform;
	
	/**
	 * Черга авто, що чекають завантаження
	 * 
	 */
	private QueueForTransactions<Cars> queueToLoader;

	
	/**
	 * Черга барж, що чекають розвантаження
	 * 
	 */
	private QueueForTransactions<Barge> Barge_queueToLoader;
	
	
	private QueueForTransactions<Cars> queueCarsonRoad;
	/**
	 * Тривалість роботи грузчиків
	 */
	private double finishTime;

	/**
	 * Генератор часу, що витрачається на одну порцію грунту
	 * 
	 */
	private Randomable rnd;


	private BooleanSupplier isBarge;

	
	private BooleanSupplier isCar;

	/**
	 * Коструктор, у якому ініціалізується гісограма для накоптчення часу
	 * простою
	 * 
	 */
	// max containers that platform can take
	private double platform_max_size;
	
	private double NumbeOfCars;
	
	private double NumbeOfBarge;
	
	public Loader(String name, RiverPortGUI gui, RiverModel model) {
		setNameForProtocol(name);
		queueToLoader = model.getQueueToLoader();
		Barge_queueToLoader = model.getBargeQueueToLoader();
		queueCarsonRoad = model.getQueueLorryOnRoad();
		NumbeOfCars = gui.getChooseDataCarsNumber().getDouble();
		finishTime = gui.getChooseDataFinishTime().getDouble();
		rnd = gui.getRndLoader();
		setHistoForActorWaitingTime(model.histoLoader);
		platform_max_size = 20;
		platform = model.getPlatform();
		
	}

	
	protected void rule() throws DispatcherFinishException {
		
		
		isBarge = () -> Barge_queueToLoader.size() > 0;
		isCar = () -> queueToLoader.size() > 0;
		
		System.out.println(queueToLoader.size());
		// Цикл виконанння правил дії навантажувача
		while (getDispatcher().getCurrentTime() <= finishTime) {
			if(Barge_queueToLoader.size() > 0) {
				waitForCondition(isBarge, "має бути баржа");
				System.out.println(Barge_queueToLoader.size() + " - Barge queue size");
				Barge barge = Barge_queueToLoader.removeFirst();
				while(!barge.isEmpty()) {
					if(queueCarsonRoad.size() < NumbeOfCars) {
						System.out.println(queueToLoader.size() + "queueToLoader");
						System.out.println(queueCarsonRoad.size() + "queueOnRoad");
						waitForCondition(isCar, "має бути авто");
						Cars cars = queueToLoader.removeFirst();
						while (!cars.isFull()) {
							getDispatcher().printToProtocol(getNameForProtocol() + " беруть контейнери");
							barge.RemoveContainer();
							// Затримка на час перевантаження
							holdForTime(rnd.next());
							// Переносимо контейнери до авто
							cars.addPortion();
							getDispatcher().printToProtocol(
									getNameForProtocol() + " переносить контейнер до авто " + cars.getNameForProtocol());
							System.out.println(barge.SizeConteiner() + "Size container");
								
							}
					}
					
						if((queueCarsonRoad.size() == NumbeOfCars) && (platform.getSize() < platform_max_size)) {
							barge.RemoveContainer();
							// Затримка на час перевантаження
							holdForTime(rnd.next());
							platform.add(1);
							System.out.println(platform.getSize() + "platform size");
								
						}
						
						if((queueCarsonRoad.size() == NumbeOfCars) && (platform.getSize() == platform_max_size)) {
							waitForCondition(isCar, "має бути авто");
						}
				}
				
			}
			
			if((Barge_queueToLoader.size() == 0) && (platform.getSize() > 0)) {
				waitForCondition(isCar, "має бути авто");
				Cars cars = queueToLoader.removeFirst();
				holdForTime(rnd.next());
				platform.remove(1);
				cars.addPortion();
				getDispatcher().printToProtocol(
						getNameForProtocol() + " переносить контейнер до авто " + cars.getNameForProtocol());
			}
			
			if((Barge_queueToLoader.size() == 0) && ((platform.getSize() == platform_max_size) || (platform.getSize() == 0))) {
				waitForCondition(isBarge, "має бути баржа");
			}
			
			
		}
						
			
	}
	
					
	/**
	 * Цей метод потрібен для корегування тривалості моделювання під час
	 * дослідження перехідних процесі
	 * 
	 * @param finishTime
	 *            - час моделювання, що сформує TransProcessManager
	 */
	public void setFinishTime(double finishTime) {
		this.finishTime = finishTime;

	}
	
	public void FormPlatform() {
		for(int i = 0; i <= platform_max_size; i++) {
			platform.add(1);
		}
	}
}

