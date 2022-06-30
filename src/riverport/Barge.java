package riverport;

import java.util.function.BooleanSupplier;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import process.Store;
import rnd.Randomable;

/**
 * Клас для абстракції "Бульдозер". Абстракція «бульдозер» моделює робочу
 * машину, що додає до купи порції ґрунту, збільшуючи таким чином її розмір.
 * Одноразова порція ґрунту в реальній системі, звичайно, є випадковою
 * величиною. Але купа ґрунту накопичує ці порції, тобто інтегрує їх, і таким
 * чином зменшує вплив коливань розміру порції. Тому, для спрощення моделі
 * будемо вважати, що порція ґрунту має стале значення і дорівнює одиниці. Тобто
 * одиницею виміру кількості ґрунту у системі буде середнє значення порції
 * ґрунту, що бульдозер додає до купи за один раз. На видобування порції ґрунту
 * бульдозер витрачає деякий час, що є випадковою величиною. Найбільш вірогідно,
 * що ця величина підпорядковується закону Ерланга. Бульдозер припиняє свою
 * роботу, якщо розмір купи збільшується до деякого критичного розміру і
 * відновлює роботу тільки після того, як розмір купи стає удвічі меншим за
 * критичний розмір.
 * 
 *
 */

public class Barge extends Actor {
	
	private Store container;

	// Кількість контейнерів на баржі
	private double NumOfConteiners;

	private double NumOfBarge;
	//Тривлістьсть роботи бульдозера
	private double finishTime;

	// Генератор часу, що витрачає бульдозер на одну порцію грунту
	private Randomable rnd;

	// Умова, після виконання якої бульдозер відновлює роботу.
	private BooleanSupplier Empty;

	private QueueForTransactions<Barge> BargeQueueToLoader;
	
	private RiverModel model;
	// Коструктор, у якому ініціалізуються поля об'єкту 
	public Barge(String name, RiverPortGUI gui, RiverModel model) {
		this.model = model;
		setNameForProtocol(name);
		
		finishTime = gui.getChooseDataFinishTime().getDouble();
		NumOfConteiners = gui.getChooseDataCapacityContainer().getDouble();
		System.out.println("form completed");
		BargeQueueToLoader = model.getBargeQueueToLoader();
		NumOfBarge = gui.getChooseDataBargeNumber().getDouble();
		rnd = gui.getRndBarge();
		setHistoForActorWaitingTime(model.histoBarge);
		
	}

	// Правила дії бульдозера. Бульдозер видобує порції ґрунту, витрачаючи на це
	protected void rule() throws DispatcherFinishException {
		container = new Store("Кількість контейнерів", model.getDispatcher(), model.gethistoPlatform());
		holdForTime(rnd.next());
		getDispatcher().printToProtocol("  " + getNameForProtocol() + " Прибула баржа в порт");
		FormConteiners();
		BargeQueueToLoader.addLast(this);
	}

	// Метод для корегування тривалості моделювання
		public void setFinishTime(double finishTime) {
			this.finishTime = finishTime;
		}
		
		
		public boolean isEmpty() {
			return container.getSize() == 0;
			
		}
		
		public void RemoveContainer(){
			container.remove(1);
		}
		
		public void FormConteiners() {
			for(int i = 0; i < NumOfConteiners; i++) {
				container.add(1);
			}
		}
		
		
		public double SizeConteiner() {
			return container.getSize();
			
		}
		
}
