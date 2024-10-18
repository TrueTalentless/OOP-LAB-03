import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Лебедев Игнат 3312
 * @version 1.0
 */
public class Main {
    // Объявление графических компонентов
    private JFrame mainFrame;
    private DefaultTableModel tableModel;
    private JTable dataTable;
    private JButton addDogButton, editDogButton, deleteDogButton, loadDogButton, saveDogButton;
    private JTextField searchField;
    private JComboBox<String> searchCriteriaComboBox;
    private boolean unsavedChanges = false; // Флаг для отслеживания несохраненных изменений

    /**
     * Метод для построения и визуализации экранной формы.
     */
    public void show() {
        // Создание основного окна
        mainFrame = new JFrame("Dog Show Administration");
        mainFrame.setSize(800, 400);
        mainFrame.setLocation(100, 100);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Слушатель для закрытия окна добавлен ниже

        // Создание основного контейнера с BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Создание кнопок для панели инструментов
        addDogButton = new JButton("Добавить");
        editDogButton = new JButton("Изменить");
        deleteDogButton = new JButton("Удалить");
        loadDogButton = new JButton("Загрузить");
        saveDogButton = new JButton("Сохранить");

        // Создание панели инструментов и добавление кнопок
        JToolBar toolBar = new JToolBar("Панель инструментов");
        toolBar.add(addDogButton);
        toolBar.add(editDogButton);
        toolBar.add(deleteDogButton);
        toolBar.add(loadDogButton);
        toolBar.add(saveDogButton);

        // Добавление панели инструментов в верхнюю часть mainPanel
        mainPanel.add(toolBar, BorderLayout.NORTH);

        // Создание таблицы с данными о собаках
        String[] columns = {"Кличка", "Порода", "Владелец", "Судья", "Награды"};
        String[][] data = {
                {"Рекс", "Немецкая овчарка", "Иванов И.И.", "Петров П.П.", "Лучший в породе"},
                {"Барон", "Доберман", "Петров П.П.", "Сидоров С.С.", "Нет наград"},
                {"Лесси", "Колли", "Смирнова А.А.", "Кузнецов К.К.", "Чемпион"},
                {"Бобик", "Бигль", "Ковалев В.В.", "Иванов И.И.", "Нет наград"}
        };
        tableModel = new DefaultTableModel(data, columns);
        dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);

        // Добавление таблицы в центральную часть mainPanel
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Создание элементов для простого поиска
        searchCriteriaComboBox = new JComboBox<>(new String[]{"По породе", "По владельцу", "По судье"});
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Поиск");

        // Создание панели поиска и фильтрации
        JPanel searchPanel = new JPanel();
        searchPanel.add(searchCriteriaComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Добавление панели поиска в нижнюю часть mainPanel
        mainPanel.add(searchPanel, BorderLayout.SOUTH);

        // Добавление mainPanel в главное окно
        mainFrame.add(mainPanel);

        /**
         * Слушатель для кнопки "Добавить".
         */
        addDogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Добавление новой записи в таблицу
                tableModel.addRow(new Object[]{"Новая собака", "Неизвестная порода", "Новый владелец", "Новый судья", "Нет наград"});
                unsavedChanges = true; // Устанавливаем флаг при изменении данных
                JOptionPane.showMessageDialog(mainFrame, "Добавлена новая собака");
            }
        });

        /**
         * Слушатель для кнопки "Изменить".
         */
        editDogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unsavedChanges = true; // Устанавливаем флаг при изменении данных
                JOptionPane.showMessageDialog(mainFrame, "Изменена информация о собаке");
            }
        });

        /**
         * Слушатель для кнопки "Удалить".
         */
        deleteDogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = dataTable.getSelectedRow(); // Получение индекса выбранной строки
                if (selectedRow != -1) {
                    int response = JOptionPane.showConfirmDialog(mainFrame, "Вы уверены, что хотите удалить эту запись?", "Подтверждение удаления", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(selectedRow); // Удаление строки
                        unsavedChanges = true; // Устанавливаем флаг при изменении данных
                        JOptionPane.showMessageDialog(mainFrame, "Запись удалена");
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Пожалуйста, выберите строку для удаления");
                }
            }
        });

        /**
         * Слушатель для кнопки "Сохранить".
         */
        saveDogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataToFile(); // Сохранение данных
            }
        });

        /**
         * Слушатель для закрытия окна.
         */
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (unsavedChanges) {
                    int response = JOptionPane.showConfirmDialog(mainFrame, "Есть несохраненные изменения. Хотите сохранить перед выходом?", "Несохраненные изменения", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        saveDataToFile();  // Логика сохранения данных
                        mainFrame.dispose();
                    } else if (response == JOptionPane.NO_OPTION) {
                        mainFrame.dispose(); // Закрыть окно без сохранения
                    }
                } else {
                    mainFrame.dispose(); // Закрыть окно, если изменений нет
                }
            }
        });

        // Отображение окна
        mainFrame.setVisible(true);
    }

    /**
     * Метод для сохранения данных в файл.
     */
    private void saveDataToFile() {
        // Логика сохранения данных в файл
        JOptionPane.showMessageDialog(mainFrame, "Данные сохранены");
        unsavedChanges = false; // Сбрасываем флаг после сохранения
    }

    /**
     * Главный метод для запуска приложения.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        // Создание и отображение формы
        new Main().show();
    }
}
