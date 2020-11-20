import java.util.Date;

public class MyClass02 {
    public static void main(String[] args)
            throws InterruptedException {
        Thread[] arr = new Thread[2];
        Message m = new Message();
        arr[0] = new Thread(new MyThreadReader(m));
        arr[0].start();
        arr[1] = new Thread(new MyThreadWriter(m));
        arr[1].start();
        Thread.sleep(10000);  // 10 секунд на выполнение
        for (Thread t: arr) { // Завершаем работу потоков
            t.interrupt();
        }
        System.out.println("Выход. Поток main");
    }
}

class Message {
    private String message;
    public synchronized String take()
            throws InterruptedException {
        while (this.message == null) {
            this.wait();   // Ждем записи, прежде чем прочитать
        }
        String result = this.message;
        this.message = null;
        this.notifyAll(); // Оповещаем о прочтении
        return result;
    }
    public synchronized void put(String message)
            throws InterruptedException {
        while (this.message != null) {
            this.wait();   // Ждем прочтения, прежде чем записать
        }
        this.message = message;
        this.notifyAll(); // Оповещаем о записи
    }
}

class MyThreadReader implements Runnable { // Поток-читатель
    private Message m;
    MyThreadReader(Message m) {
        this.m = m;
    }
    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Прочитано " + m.take());
                Thread.sleep(500);
            }
            catch (InterruptedException e) { return; }
        }
    }
}

class MyThreadWriter implements Runnable { // Поток-писатель
    private Message m;
    MyThreadWriter(Message m) {
        this.m = m;
    }
    @Override
    public void run() {
        while (true) {
            try {
//                m.put(String.valueOf(Math.random()));
                m.put(String.valueOf(new Date()));
                Thread.sleep(1000);
            }
            catch (InterruptedException e) { return; }
        }
    }
}