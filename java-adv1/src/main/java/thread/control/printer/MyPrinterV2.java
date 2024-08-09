package thread.control.printer;import static thread.util.MyLogger.log;import static thread.util.ThreadUtils.sleep;import java.util.Queue;import java.util.Scanner;import java.util.concurrent.ConcurrentLinkedQueue;public class MyPrinterV2 {    public static void main(String[] args) {        Printer printer = new Printer();        Thread thread = new Thread(printer, "printer");        thread.start();        Scanner userInput = new Scanner(System.in);        while (true) {            log("프린터할 문서를 입력하세요. 종료 (q): ");            String job = userInput.nextLine();            if (job.equals("q")) {                printer.work = false;                thread.interrupt();                break;            }            printer.addJob(job);        }    }    static class Printer implements Runnable {        volatile boolean work = true;        Queue<String> jobQueue = new ConcurrentLinkedQueue<>(); // 여러 스레드가 동시에 접근하는 queue 는 ConcurrentLinkedQueue 를 사용해야 한다.        @Override        public void run() {            while (work) {                if (jobQueue.isEmpty()) {                    continue;                }                try {                    String job = jobQueue.poll();                    log("출력 시작: " + job + ", 대기 문서: " + jobQueue);                    Thread.sleep(3000); //출력에 걸리는 시간                    log("출력 완료: " + job);                } catch (InterruptedException e) {                    log("인터럽트!");                    break;                }            }            log("프린터 종료");        }        public void addJob(String job) {            jobQueue.add(job);        }    }}