<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    </head>
<body>
    <h1>Система учета бюджета</h1>    
    <div class="section">
        <h2>Основные сущности</h2>        
        <h3>Запись бюджета (<code>BudgetRecord</code>)</h3>
        <ul>
            <li><strong>Идентификатор</strong> (<code>id</code>): Уникальный идентификатор записи</li>
            <li><strong>Год</strong> (<code>year</code>): Год, к которому относится запись</li>
            <li><strong>Месяц</strong> (<code>month</code>): Месяц записи</li>
            <li><strong>Сумма</strong> (<code>amount</code>): Денежная сумма</li>
            <li><strong>Тип бюджета</strong> (<code>budgetType</code>): Тип записи (доход/расход)</li>
            <li><strong>Автор</strong> (<code>author</code>): Ссылка на автора записи</li>
            <li><strong>Дата создания</strong> (<code>createRecord</code>): Время создания записи</li>
        </ul>        
        <h3>Автор (<code>Author</code>)</h3>
        <ul>
            <li><strong>Идентификатор</strong> (<code>id</code>): Уникальный идентификатор автора</li>
            <li><strong>ФИО</strong> (<code>fio</code>): Полное имя автора</li>
            <li><strong>Дата создания</strong> (<code>createData</code>): Время создания записи об авторе</li>
        </ul>
    </div>    
    <div class="section">
        <h2>Сервисные компоненты</h2>        
        <h3><code>BudgetServiceImpl</code></h3>        
        <h4>Создание записи (<code>create</code>)</h4>
        <p>Принимает данные записи и ID автора:</p>
        <ul>
            <li>Проверяет корректность типа бюджета</li>
            <li>Связывает запись с автором</li>
            <li>Сохраняет запись в базу данных</li>
            <li>Возвращает DTO с информацией о созданной записи</li>
        </ul>        
        <h4>Получение данных (<code>getBudget</code>)</h4>
        <p>Принимает параметры запроса:</p>
        <ul>
            <li>Год</li>
            <li>Лимит результатов</li>
            <li>Смещение</li>
            <li>Строка поиска (опционально)</li>
        </ul>
        <p>Выполняет поиск записей по заданным параметрам:</p>
        <ul>
            <li>Агрегирует данные:</li>
            <ul>
                <li>Общая сумма</li>
                <li>Суммы по типам бюджета</li>
            </ul>
            <li>Возвращает DTO с:</li>
            <ul>
                <li>Списком записей</li>
                <li>Общей суммой</li>
                <li>Суммами по типам</li>
            </ul>
        </ul>        
        <h3><code>AuthorServiceImpl</code></h3>
        <p>Управляет авторами записей:</p>
        <ul>
            <li>Метод <code>createAuthor</code>:</li>
            <ul>
                <li>Создаёт нового автора</li>
                <li>Сохраняет в базе данных</li>
                <li>Возвращает DTO с информацией об авторе</li>
            </ul>
        </ul>
    </div>    
    <div class="section">
        <h2>Особенности реализации</h2>        
        <h3>Транзакционность</h3>
        <p>Операции создания записи выполняются в транзакции с уровнем изоляции <code>REPEATABLE_READ</code></p>
        <p>Операции чтения выполняются в режиме <code>read-only</code></p>        
        <h3>Логирование</h3>
        <p>Все важные операции логируются:</p>
        <ul>
            <li>Включает информацию о созданных объектах</li>
            <li>Содержит данные о результатах запросов</li>
        </ul>
    </div>    
    <div class="section">
        <h2>Взаимодействие компонентов</h2>
        <p><code>BudgetServiceImpl</code> использует:</p>
        <ul>
            <li><code>BudgetRepository</code> для работы с записями бюджета</li>
            <li><code>AuthorRepository</code> для работы с авторами</li>
            <li><code>BudgetMapper</code> для преобразования данных</li>
        </ul>
        <p><code>AuthorServiceImpl</code> использует:</p>
        <ul>
            <li><code>AuthorRepository</code> для работы с авторами</li>
            <li><code>AuthorMapper</code> для преобразования данных</li>
        </ul>
    </div>    
    <div class="section">
        <h2>Дополнительные возможности</h2>
        <ul>
            <li>Пагинация результатов</li>
            <li>Сортировка по месяцу</li>
            <li>Поиск по ФИО автора</li>
            <li>Агрегирование данных по типам бюджета</li>
        </ul>
    </div>
    <h1>Стек технологий</h1>
    <div class="section">
        <h2>Языки программирования и фреймворки</h2>
        <ul>
            <li><strong>Java</strong>: Основной язык разработки проекта.</li>
            <li><strong>Spring Boot</strong>: Используется для быстрого старта приложения и упрощенной настройки сервисов.</li>
        </ul>
    </div>    
    <div class="section">
        <h2>Модули и библиотеки</h2>
        <ul>
            <li><strong>Spring Web</strong>: Поддерживает создание RESTful API.</li>
            <li><strong>Spring Validation</strong>: Валидация входных данных через аннотации.</li>
            <li><strong>Spring Data JPA</strong>: Упрощенная работа с базой данных посредством ORM.</li>
            <li><strong>PostgreSQL</strong>: Реляционная база данных, используемая в проекте.</li>
            <li><strong>Liquibase</strong>: Управление миграциями базы данных.</li>
            <li><strong>Lombok</strong>: Автоматическая генерация getters/setters, конструкторов и прочих стандартных методов.</li>
            <li><strong>MapStruct</strong>: Библиотека для автоматического маппинга объектов.</li>
            <li><strong>OpenAPI</strong>: Генерация документации для REST API.</li>
            <li><strong>Hibernate Validator</strong>: Аннотированный валидатор для проверки ограничений в моделях.</li>
        </ul>
    </div>    
    <div class="section">
        <h2>Инструменты сборки и тестирования</h2>
        <ul>
            <li><strong>Maven</strong>: Система управления зависимостями и сборкой проекта.</li>
            <li><strong>JUnit</strong>: Тестовый фреймворк для юнит-тестирования.</li>
        </ul>
    </div>
</body>
</html>
