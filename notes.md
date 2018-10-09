### 1. OOP - tradycyjne podejście obiektowe
Problem:
  * pomieszanie domeny z logiką serializacji
  * mała elastyczność serializacji (kompaktowy/formatowany json)

### 2. BetterJsonPrinting - formatowanie w osobnej funkcji, drzewko jsona osobno
- lepiej, bo można mieć więcej niż jedną funkcję formatującą
- inne funkcje mogą robić coś innego niż zamieniąć na stringa, np. konwertować na inny ML (i nie zaśmiecamy typów)
- czysty model
Nierozwiązany problem: zaśmiecona domena (przeciążona metoda toJson)

### 3. DomainJsonSeparation - Czysta domena, serializacja w osobnym module
- serializacja ciągle nie jest elastyczna, nie możemy napisać `def toJson[A](s: Seq[A]): JsonValue`

### 4. FirstTypeclass
- niewielkia zmiana, zamiast funkcji typ, generyczna funkcja formatCompactJson

### 5. ImplicitTypeclass
- podobnie jak poprzednio, bez bezpośrendniego wstrzykiwania instancji typeclass

### 6. TypeclassForExternalType
- rozszerzanie typów, na które nie mamy wpływu o nową funkcję - tu serializacja do jsona, możliwość przeniesienia tego typu logiki do osobnego modułu

### 7. GenericDerivation
- automatyczne tworzenie typeclass na podstawie case class. Podstawą jest shapeless i HListy (heterogeniczne listy)

