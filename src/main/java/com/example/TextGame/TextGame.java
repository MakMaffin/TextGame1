package com.example.TextGame;

import java.io.*;
import java.io.IOException;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TextGame implements Serializable {
    private static final Logger LOGGER = LogManager.getLogger(TextGame.class);
    private JFreeChart lineChart;
    private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private int day = 0;
    private static final int SIZE = 6;
    private static final int WATER_START = 10;
    private static final int RICE_START = 10;
    private static final int CELLS_START = 1;
    private static final int HOUSES_START = 0;
    private static final int PEASANTS_START = 1;
    private static final int WIN_THRESHOLD = SIZE * SIZE / 2;
    private int[][] field;
    private String[][] map;
    private int playerWater;
    private double playerRice;
    private int playerCells;
    private int playerHouses;
    private int playerPeasants;
    private int aiWater;
    private double aiRice;
    private int aiCells;
    private int aiHouses;
    private int aiPeasants;
    private boolean playerTurn;
    private int PlayerX;
    private int PlayerY;

    private int AIX;
    private int AIY;
    private JFrame frame;

    /**
     * Это конструктор класса
     * Здесь выдаются начальные значения и выводится начальный, пустой график для последующих обновлений
     */
    public TextGame() {
        this.field = new int[SIZE][SIZE];
        this.map = new String[SIZE][SIZE];
        this.playerWater = WATER_START;
        this.playerRice = RICE_START;
        this.playerCells = CELLS_START;
        this.playerHouses = HOUSES_START;
        this.playerPeasants = PEASANTS_START;

        this.PlayerX = 5;
        this.PlayerY = 5;

        this.AIX = 0;
        this.AIY = 0;


        this.aiWater = WATER_START;
        this.aiRice = RICE_START;
        this.aiCells = CELLS_START;
        this.aiHouses = HOUSES_START;
        this.aiPeasants = PEASANTS_START;

        this.playerTurn = true;

        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++){
                field[i][j] = (int) (Math.random()*6);
            }
       }
        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++){
                map[i][j] = Integer.toString(field[i][j]);
            }
        }

        placeInitialCells("A", "P");

        frame = new JFrame("Resource Graph");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });
        showResourceGraph(playerWater, playerRice, aiWater, aiRice, day);
    }

    /**
     * Этот метод нужен для размещения изначальных положений игроков.
     * @param player1 Название первого игрока
     * @param player2 Название второго игрока
     */
    private void placeInitialCells(String player1, String player2) {
        map[SIZE - 1][SIZE - 1] = player2;
        map[0][0] = player1;
    }

    /**
     * Этот метод нужен для взаимодействия с игроком.
     * То есть, пока мы в системе, будет воспроизводиться цикл с обновлением ресурсов, сменой хода
     */
    public void play() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            printStatus();
            if (playerTurn) {
                System.out.println("\nВаш ход:");
                playerAction(reader);
            } else {
                System.out.println("\nХод AI:");
                aiAction(reader);
            }
            updateResources();
            checkWin();
            day++;
            showResourceGraph(playerWater, playerRice, aiWater, aiRice, day);
            switchTurn();
        }
    }

    /**
     * Этот метод выводит график всех ресурсов при вызове в отдельное окно.
     * Метод запрашивает каждый ресурс в игре и, соответственно, выводит их в отдельное окно
     * @param playerWater Количество воды у игрока
     * @param playerRice Количесвто риса у игрока
     * @param aiWater Количесвто воды у AI
     * @param aiRice Количесвто риса у AI
     * @param day Номер дня
     */
    private void showResourceGraph(int playerWater, double playerRice, int aiWater, double aiRice, int day) {
        dataset.addValue(playerWater, "Player Water", "Day " + day);
        dataset.addValue(aiWater, "AI Water", "Day " + day);
        dataset.addValue(playerRice, "Player Rice", "Day " + day);
        dataset.addValue(aiRice, "AI Rice", "Day " + day);
        dataset.addValue(playerCells, "Player Cells", "Day " + day);
        dataset.addValue(playerHouses, "Player Houses", "Day " + day);
        dataset.addValue(playerPeasants, "Player Peasants", "Day " + day);
        dataset.addValue(aiCells, "AI Cells", "Day " + day);
        dataset.addValue(aiHouses, "AI Houses", "Day " + day);
        dataset.addValue(aiPeasants, "AI Peasants", "Day " + day);
        if (frame == null) {
        } else {
            // Обновляем график с новыми данными
            lineChart = ChartFactory.createLineChart(
                    "Resource Overview", // Заголовок графика
                    "Day", "Amount", // Оси X и Y
                    dataset
            );
            ChartPanel chartPanel = new ChartPanel(lineChart);
            frame.setContentPane(chartPanel);
            frame.pack();
            frame.setVisible(true);
        }
    }
    /**
     * Этот метод выводит информацию о правилах игры
     * @param reader Буфер
     */
    private void getInfo(BufferedReader reader) {
        System.out.println("\n1. При смене хода, количество риса повышается на текущее количество крестьян");
        System.out.println("2. При поливе риса, количество риса повышается на текущее количество крестьян, умноженное на 2");
        System.out.println("3. При сборе воды, количество воды повышается на 5");
        System.out.println("4. Стоимость постройки дома - 5 риса, 5 воды и 1 крестьянин");
        System.out.println("5. Каждый ход количество крестьян увеличивается текущее количество домов");
        System.out.println("6. При захвате 50% клеток объявляется победитель и игра заканчивается");
        System.out.println("7. Захват клетки осуществляется по цене: 3 воды, 5 риса, 1 крестьянин");
        playerAction(reader);
    }

    /**
     * Этот метод выводит текущее состояние игры в консоль.
     * Метод запрашивает каждый ресурс в игре и, соответственно, выводит их в консоль
     */
    private void printStatus() {
        System.out.println("\n=== Статус игры ===");
        System.out.println("Ваш ресурс воды: " + playerWater);
        System.out.println("Ваш ресурс риса: " + playerRice);
        System.out.println("Ваши клетки: " + playerCells);
        System.out.println("Ваши дома: " + playerHouses);
        System.out.println("Ваши крестьяне: " + playerPeasants);

        System.out.println("\nAI ресурс воды: " + aiWater);
        System.out.println("AI ресурс риса: " + aiRice);
        System.out.println("AI клетки: " + aiCells);
        System.out.println("AI дома: " + aiHouses);
        System.out.println("AI крестьяне: " + aiPeasants);

        System.out.println("\n=== Поле ===");
        for (String[] row : map) {
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }

    }

    /**
     * Этот метод заканчивает игру.
     */
    private void exitGame() {
        System.out.println("Игра завершена. До новых встреч!");
        System.exit(0); // Выход из игры
    }

    /**
     * Этот метод обрабатывает действия игрока при выборе той или иной цифры в консоль, вызывая метод, соответствующий выбору
     * @param reader Буфер
     */
    private void playerAction(BufferedReader reader) {
        try {
            System.out.println("Выберите действие:");
            System.out.println("1. Набрать воды");
            System.out.println("2. Полить рис");
            System.out.println("3. Захватить пустую клетку");
            System.out.println("4. Построить дом");
            System.out.println("5. Сохранить игру");
            System.out.println("6. Загрузить игру");
            System.out.println("7. Очистить файл сохранения");
            System.out.println("8. Выведи график ресурсов");
            System.out.println("9. Справка");
            System.out.println("0. Завершить игру");
            String input = reader.readLine();

            int choice = -1;
            if (choice < 0 || choice > 9) {
                System.out.print("Ваш выбор: ");
                try {
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 9) {
                        System.out.println("Пожалуйста, введите корректное значение (число от 0 до 9).");
                        playerAction(reader);
                        LOGGER.error("Incorrect number");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат. Пожалуйста, введите число от 0 до 9.");
                    LOGGER.error("Incorrect input");
                    playerAction(reader);
                }
            }

            switch (choice) {
                case 1:
                    collectWater(playerWater, playerRice, playerPeasants);
                    LOGGER.info("Input 1: Collect water");
                    break;
                case 2:
                    waterRice(playerWater, playerRice, playerPeasants, reader);
                    LOGGER.info("Input 2: Water the rice");
                    break;
                case 3:
                    captureCell(playerWater, playerRice, playerPeasants, playerTurn, field, playerCells, reader);
                    LOGGER.info("Input 3: Capture an empty cell");
                    break;
                case 4:
                    buildHouse(playerWater, playerRice, playerHouses, playerPeasants, reader);
                    LOGGER.info("Input 4: Build a house");
                    break;
                case 5:
                    saveGame("savegame.ser", reader);
                    LOGGER.info("Input 5: Save game");
                    break;
                case 6:
                    loadGame("savegame.ser", reader);
                    LOGGER.info("Input 6: Load game");
                    break;
                case 7:
                    clearSaveFile("savegame.ser", reader);
                    LOGGER.info("Input 7: Clear save file");
                    break;
                case 8:
                    showResourceGraph(playerWater, playerRice, aiWater, aiRice, day);
                    playerAction(reader);
                    LOGGER.info("Input 8: Show graph");
                    break;
                case 9:
                    LOGGER.info("Input 9: Get info");
                    getInfo(reader);
                case 0:
                    LOGGER.info("Closing program...");
                    exitGame();
                default:
                    System.out.println("");
                    playerAction(reader);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Этот метод обрабатывает действия AI через нижеуказанные условия
     * Метод запрашивает каждый ресурс в игре и затем работает с ними
     * @param reader Буфер
     */
    private void aiAction(BufferedReader reader) {
        if (aiWater < 6) {
            collectWater(aiWater, aiRice, aiPeasants);
        } else if (aiHouses < 2) {
            buildHouse(aiWater, aiRice, aiHouses, aiPeasants, reader);
        } else if (aiRice < 10) {
            waterRice(aiWater, aiRice, aiPeasants, reader);
        } else  if (aiWater < 5 || aiRice < 5) {
            System.out.println("AI не имеет достаточно ресурсов для захвата клетки.");
        }else {

            int aiX = AIX;
            int aiY = AIY;
            int capturedX = aiX;
            int capturedY = aiY;
            boolean captured = false;

            for (int i = aiX - 1; i <= aiX + 1; i++) {
                for (int j = aiY - 1; j <= aiY + 1; j++) {
                    if (i >= 0 && i < SIZE && j >= 0 && j < SIZE) {
                        if (field[i][j] <= aiPeasants && !(i == aiX && j == aiY) && map[i][j] != "A" && map[i][j] != "P") {
                            capturedX = i;
                            capturedY = j;
                            captured = true;
                            break;
                        }
                    }
                }
                if (captured) {
                    break;
                }
            }

            if (captured) {
                map[aiX][aiY] = "A";
                map[capturedX][capturedY] = "A";
                System.out.println("AI захватил клетку");

                LOGGER.info("AI captured a cell");

                aiWater -= 3;
                aiRice -= 5;
                aiPeasants -= field[capturedX][capturedY];
                aiCells++;
                this.AIX = capturedX;
                this.AIY = capturedY;

                checkWin();
            } else {
                System.out.println("AI не может захватить клетку.");
            }
        }
    }

    /**
     * Этот метод увеличивает ресурс воды
     * @param waterResource Количество воды у игрока
     * @param riceResource Количество риса у игрока
     * @param peasantsResource Количество крестьян у игрока
     */
    private void collectWater(int waterResource, double riceResource, int peasantsResource) {
        waterResource += 5;
        System.out.println("Набрано воды.");

        if (playerTurn) {
            playerWater = waterResource;
        } else {
            aiWater = waterResource;
        }
    }

    /**
     * Этот метод увеличивает ресурс риса
     * @param waterResource Количество воды у игрока
     * @param riceResource Количество риса у игрока
     * @param peasantsResource Количество крестьян у игрока
     * @param reader Буфер
     */
    private void waterRice(int waterResource, double riceResource, int peasantsResource, BufferedReader reader) {
        if (waterResource >= 3) {
            waterResource -= 3;
            riceResource += 2 * peasantsResource;
            System.out.println("Рис успешно полит.");

            LOGGER.info("Rice successfully watered");

            if (playerTurn) {
                playerWater = waterResource;
                playerRice = riceResource;
            } else {
                aiWater = waterResource;
                aiRice = riceResource;
            }
        } else {
            System.out.println("Недостаточно воды для полива риса.");
            LOGGER.info("Not enough water to water rice");
            playerAction(reader);
        }
    }

    /**
     * Этот метод захватывает клетку с учётом выбранного направления.
     * Метод запрашивает все возможные ресурсы, кроме дней, и далее работает с ними
     * @param waterResource Количество воды у игрока
     * @param riceResource Количество риса у игрока
     * @param peasantsResource Количество крестьян у игрока
     * @param playerTurn Очередь хода
     * @param field Карта игры
     * @param reader Буфер
     */
    private void captureCell(int waterResource, double riceResource, int peasantsResource, boolean playerTurn, int[][] field, int cellsResource, BufferedReader reader) {
        if (waterResource < 3 || riceResource < 5 || peasantsResource < field[PlayerX][PlayerY]) {
            System.out.println("Недостаточно ресурсов для захвата клетки");
            LOGGER.info("Not enough resources to capture a cell");
            playerAction(reader);
            return;
        }
        int currentPlayerX = PlayerX;
        int currentPlayerY = PlayerY;

        try {
            System.out.println("Выберите направление (w - вверх, s - вниз, a - влево, d - вправо, q - назад):");
            String input = reader.readLine().toLowerCase();
            int xOffset = 0;
            int yOffset = 0;

            if (input.equals("w")) {
                xOffset = -1;
            } else if (input.equals("s")) {
                xOffset = 1;
            } else if (input.equals("a")) {
                yOffset = -1;
            } else if (input.equals("d")) {
                yOffset = 1;
            } else if (input.equals("q")) {
                playerAction(reader);
                return;
            } else {
                System.out.println("Выбрано недопустимое направление!");
                LOGGER.error("Invalid direction selected");
                playerAction(reader);
                return;
            }

            int newX = currentPlayerX + xOffset;
            int newY = currentPlayerY + yOffset;

            if (newX < 0 || newX > 5 || newY < 0 || newY > 5) {
                System.out.println("Выход за пределы поля!");
                LOGGER.error("Out of bounds");
                playerAction(reader);
                return;
            }
            if (map[newX][newY] == "P" || map[newX][newY] == "A") {
                System.out.println("Клетка уже занята!");
                LOGGER.error("Cell is already captured");
                playerAction(reader);
                return;
            }

            waterResource -= 3;
            riceResource -= 5;
            peasantsResource -= field[currentPlayerX][currentPlayerY];
            cellsResource++;
            System.out.println("Захвачена клетка.");
            LOGGER.info("Cell is captured");
            checkWin();
            if (playerTurn) {
                playerWater = waterResource;
                playerRice = riceResource;
                playerPeasants = peasantsResource;
                playerCells = cellsResource;
            } else {
                aiWater = waterResource;
                aiRice = riceResource;
                aiPeasants = peasantsResource;
                aiCells = cellsResource;
            }


            map[currentPlayerX][currentPlayerY] = "P";
            map[newX][newY] = "P";
            this.PlayerX = newX;
            this.PlayerY = newY;
        } catch (IOException e) {
            System.err.println("Ошибка ввода");
            LOGGER.error("Incorrect input");
            System.out.println("Недопустимый ввод. Возвращаемся к выбору действия игрока.");
            playerAction(reader);
        }
    }

    /**
     * Этот метод увеличивает количество домов за счёт обмена на определённое количество ресурсов
     * @param waterResource Количество воды у игрока
     * @param riceResource Количество риса у игрока
     * @param housesResource Количество домов у игрока
     * @param peasantsResource Количество крестьян у игрока
     */
    private void buildHouse(int waterResource, double riceResource, int housesResource, int peasantsResource, BufferedReader reader) {
        if (riceResource >= 5 && waterResource >= 5) {
            riceResource -= 5;
            waterResource -= 5;
            housesResource++;
            System.out.println("Дом построен. Прибавлено 1 крестьянин.");

            LOGGER.info("House is built. 1 peasant added");

            if (playerTurn) {
                playerRice = riceResource;
                playerHouses = housesResource;
                playerPeasants = peasantsResource;
                playerWater = waterResource;
            } else {
                aiRice = riceResource;
                aiHouses = housesResource;
                aiPeasants = peasantsResource;
                aiWater = waterResource;
            }
        } else {
            System.out.println("Недостаточно риса для постройки дома.");
            LOGGER.error("Not enough rice to build a house");
        }
        playerAction(reader);
    }

    /**
     * Этот метод увеличивает количество ресурсов риса каждый ход.
     */
    private void updateResources() {
        if (playerTurn) {
            playerRice += playerPeasants;
            playerPeasants += playerHouses;
        } else {
            aiRice += aiPeasants;
            aiPeasants += aiHouses;
        }
    }

    /**
     * Этот метод проверяет условия победы
     */
    private void checkWin() {
        if (playerCells >= WIN_THRESHOLD) {
            System.out.println("Вы выиграли! Захвачено 50% поля.");
            LOGGER.info("Player won");
            System.exit(0);
        } else if (aiCells >= WIN_THRESHOLD) {
            System.out.println("AI выиграл. Захвачено 50% поля.");
            LOGGER.info("AI won");
            System.exit(0);
        }
    }

    /**
     * Этот метод меняет ход игрока на ход ИИ
     */
    private void switchTurn() {
        playerTurn = !playerTurn;
    }


    /**
     * Этот метод сохраняет данные о ресурсах и клетках в файл, обращаясь к другому классу
     * @param filename Имя файла
     */
    public void saveGame(String filename, BufferedReader reader) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            GameSave save = new GameSave(field, playerWater, playerRice, playerCells,
                    playerHouses, playerPeasants, aiWater, aiRice,
                    aiCells, aiHouses, aiPeasants, playerTurn, map);
            out.writeObject(save);
            System.out.println("Игра сохранена в файл: " + filename);
            LOGGER.info("Game saved into a file");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении игры: " + e.getMessage());
            LOGGER.error("Error saving game ");
        }
        playerAction(reader);
    }

    /**
     * Этот метод загружает данные о ресурсах и клетках из файла, обращаясь к другому классу
     * @param filename Имя файла
     * @param reader Буфер
     */
    public void loadGame(String filename,  BufferedReader reader) {
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Файл сохранения пуст или не существует.");
            LOGGER.error("File is empty or dont exists");
            playerAction(reader);
        } else {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
                GameSave save = (GameSave) in.readObject();
                map = save.getMap();
                field = save.getField();
                playerWater = save.getPlayerWater();
                playerRice = save.getPlayerRice();
                playerCells = save.getPlayerCells();
                playerHouses = save.getPlayerHouses();
                playerPeasants = save.getPlayerPeasants();
                aiWater = save.getAiWater();
                aiRice = save.getAiRice();
                aiCells = save.getAiCells();
                aiHouses = save.getAiHouses();
                aiPeasants = save.getAiPeasants();
                playerTurn = save.isPlayerTurn();
                System.out.println("Игра загружена из файла: " + filename);
                LOGGER.info("Game loaded from file");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Ошибка при загрузке игры: " + e.getMessage());
                LOGGER.error("Error loading game");
            }
            playerAction(reader);
        }
    }

    /**
     * Этот метод позволяет очистить данные о ресурсах и клетках из файла
     * @param filename Имя файла
     */
    public void clearSaveFile(String filename, BufferedReader reader) {
        try {
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.close();
            System.out.println("Файл " + filename + " был очищен.");
            LOGGER.info("File" + filename + "was cleared");
        } catch (IOException e) {
            System.err.println("Ошибка при очистке файла: " + e.getMessage());
            LOGGER.error("Error clearing file");
        }
        playerAction(reader);
    }

    /**
     * Точка входа в программу
     */
    public static void main(String[] args) {
        TextGame game = new TextGame();
        LOGGER.info("Starting program...");
        game.play();
    }
}