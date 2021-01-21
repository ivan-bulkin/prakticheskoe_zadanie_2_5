package part_02.prakticheskoe_zadanie_5;

//После мозгового штурма я убедился, что вычисления произведены верно
//Первый метод. Сумма элементов массива после вычисления: 0.95204055
//Второй метод. Сумма элементов массива после вычисления: 0.95204055
//Также видно после выполнения программы, что скорость вычисления в двух потоках в два раза быстрее, чем в одном несмотря на склейки/расклейки массивов
//Время работы первого метода: +-1247
//Время работы второго метода: +-638

public class Main {

    static final int size = 10000000;
    static final int h = size / 2;

    public static void main(String[] args) throws InterruptedException {
        long a = System.currentTimeMillis();//Засекаем время выполнения
        System.out.println("Практическое задание к уроку 5. Многопоточность.");
        metodOne();
        metodTwo();
        System.out.println("Вся программа отработала за: " + (System.currentTimeMillis() - a));//Выводим в консоль время работы
    }

    private static void metodTwo() throws InterruptedException {
        float[] arr = new float[size];
        float[] a1 = new float[h];
        float[] a2 = new float[h];
        System.out.println("Метод два. Размер массива: " + arr.length);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1;//Заполняем массив единицами
        }
        long a = System.currentTimeMillis();//Засекаем время выполнения

//создаём новый(отдельный) поток thread1
        Thread thread1 = new Thread() {
            @Override
            //переопределяем метод run
            public void run() {
                System.out.println("Поток 1");
                System.arraycopy(arr, 0, a1, 0, h);//Копируем первую часть массива arr в массив a1
                for (int i = 0; i < a1.length; i++) {
                    a1[i] = (float) (a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));//Считаем новое значение по этой формуле
                }
            }
        };
//создаём новый(отдельный) поток thread2
        Thread thread2 = new Thread() {
            @Override
            //переопределяем метод run
            public void run() {
                System.out.println("Поток 2");
                System.arraycopy(arr, h, a2, 0, h);//Копируем вторую часть массива arr в массив a2
                for (int i = 5000000; i < a2.length+5000000; i++) {//т.к. в расчёте учавствует переменная i, то чистоты эксперимента не получится, т.к. в методе один i считаем от 1 до 10000000, а в методе два оба раза считаем i от 1 до 5000000
                    //эта заморочка с 5000000 нужна, чтобы вычислить пересчёт элементов массива правильно и для чистоты эксперимента, т.к. вычисления с большими числами должны идти медленнее, чем с маленькими
                    a2[i-5000000] = (float) (a2[i-5000000] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));//Считаем новое значение по этой формуле
                }
            }
        };
        thread1.start();//Надо именно start, а не run
        thread2.start();//Надо именно start, а не run
        thread1.join();//надо дождаться завершение выполнения потока, иначе подсчёт будет не верен
        thread2.join();//надо дождаться завершение выполнения потока, иначе подсчёт будет не верен
        //Склеиваем массив назад
        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, h, h);

        System.out.println("Время работы второго метода: " + (System.currentTimeMillis() - a));//Выводим в консоль время работы
//посчитаем сумму элементов массива, чтобы сравнить как отработал первый и второй методы
        float summ = 0;
        for (int i = 0; i < arr.length; i++) {
            summ = summ + arr[i];
        }
        System.out.println("Второй метод. Сумма элементов массива после вычисления: " + summ);//Выводим в консоль время работы
    }

    private static void metodOne() {
        float[] arr = new float[size];
        System.out.println("Метод один. Размер массива: " + arr.length);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1;//Заполняем массив единицами
//            System.out.println(i);
        }
        long a = System.currentTimeMillis();//Засекаем время выполнения
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));//Считаем новое значение по этой формуле
//            System.out.println(arr[i]);
        }
//        System.currentTimeMillis();
        System.out.println("Время работы первого метода: " + (System.currentTimeMillis() - a));//Выводим в консоль время работы
//посчитаем сумму элементов массива, чтобы сравнить как отработал первый и второй методы
        float summ = 0;
        for (int i = 0; i < arr.length; i++) {
            summ = summ + arr[i];
        }
        System.out.println("Первый метод. Сумма элементов массива после вычисления: " + summ);//Выводим в консоль время работы
    }
}
