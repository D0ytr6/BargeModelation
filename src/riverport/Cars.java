package riverport;

import java.util.function.BooleanSupplier;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import rnd.Randomable;

/**
 * Клас для абстракції Самоскид. Завданням абстракції "самоскид" є перевезення
 * ґрунту від навантажувача до замовника. Шлях до замовника та від замовника до
 * навантажувача потребує випадкових проміжків часу. Під’їхавши до
 * навантажувача,самоскидів стає у чергу і чекає, поки його завантажать. Після
 * цього знову їде до замовника. У замовника самоскид розвантажується і знову
 * їде до навантажувача. Працює самоскид впродовж усього часу моделювання.
 * Особливість поведінки самоскида полягає у тому, що окрім основної діяльності
 * він має реєструватися у списку, який містиь самоскиди, що їдуть до
 * навантажувача та від нього. Це пов’язано з вимогою завдання забезпечити
 * динамічну індикацію самоскидів, які знаходяться у дорозі.
 * 
 *
 */
public class Cars extends Actor {

	/**
	 * Тривалість роботи авто
	 */
	private double finishTime;

	/**
	 * Черга авто у дорозі
	 */
	private QueueForTransactions<Cars> queueAutoOnRoad;

	/**
	 * Черга авто до нантажувача
	 * 
	 */
	private QueueForTransactions<Cars> queueToLoader;

	/**
	 * Генератор часу, що витрачається на дорогу в один кінець
	 * 
	 */
	private Randomable rnd;

	/**
	 * Рівень завантаження авто
	 * 
	 */
	private int bodyLoad;

	private double bodysize;
	
	private BooleanSupplier isBodyFull;

	/**
	 * Коструктор, у якому ініціалізується гістограма для накопичення часу
	 * простою
	 * 
	 */
	public Cars(String name, RiverPortGUI gui, RiverModel model) {
		setNameForProtocol(name);
		this.queueToLoader = model.getQueueToLoader();
		this.queueAutoOnRoad = model.getQueueLorryOnRoad();
		this.finishTime = gui.getChooseDataFinishTime().getDouble();
		this.rnd = gui.getRndCars();
		this.setHistoForActorWaitingTime(model.histoAuto);
		this.bodysize = 2;
	}

	public void rule() throws DispatcherFinishException {
		
		isBodyFull = () -> isFull();
		// Цикл правил дії авто
		while (getDispatcher().getCurrentTime() <= finishTime) {
			// Авто їде до навантажувача
			// i реєструється у списку авто, що їдуть
			queueAutoOnRoad.addLast(this);
			System.out.println(queueAutoOnRoad.size());
			holdForTime(rnd.next());
			// вилучає себе із відповідного списку
			queueAutoOnRoad.remove(this);
			// Авто стає у чергу до навантажувача,
			queueToLoader.addLast(this);
			// Чекає поки завантажать
				waitForCondition(isBodyFull, "авто має бути повним");
			// авто їде на розвантаження
			getDispatcher().printToProtocol(getNameForProtocol() + " поїхало на розвантаження");
			// реєструється у списку авто, що їдуть
			queueAutoOnRoad.add(this);
			holdForTime(rnd.next());
			// вилучає себе із списку авто, що їдуть
			queueAutoOnRoad.remove(this);
			getDispatcher().printToProtocol(
					getNameForProtocol() + " розвантаження авто");
			// Авто розвантажується
			bodyLoad = 0;
		}
	}


	/**
	 * Метод, що додає контейнер до авто
	 * 
	 */
	public void addPortion() {
		bodyLoad++;
		getDispatcher().printToProtocol(
				getNameForProtocol() + "- у багажнику стало " + bodyLoad);
	}

	/**
	 * Метод перевірки завантаженості авто
	 * 
	 */
	public boolean isFull() {
		return bodyLoad >= bodysize;
	}


	/**
	 * Цей метод потрібен для корегування тривалості моделювання
	 * під час дослідження перехідних процесі
	 * @param finishTime - час моделювання, що сформує TransProcessManager
	 */
		public void setFinishTime(double finishTime) {
			this.finishTime = finishTime;

		}
}
