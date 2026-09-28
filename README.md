# JADE Dining Philosophers

As a side note, I chose the manager-based approach because I considered it optimal. Alternatives included breaking the symmetry—simply having one philosopher pick up the forks in a different order—or implementing a more complex scheme where philosophers reserve forks in advance; however, the latter was problematic because the philosophers might not know exactly how long they would spend eating or thinking.

One downside of this implementation is that it might not be entirely fair; the manager could consistently favor certain philosophers over others. In practice, however, I didn't encounter this issue during testing—everyone eventually got a turn to eat.

Java must be installed. JADE is already included in the `lib` directory.

## Run

Open a terminal in the project directory and run:

```bash
./run.sh
```

By default, the program starts 5 philosophers on port `1100`.

To use another port and number of philosophers:

```bash
./run.sh 1101 10
```

Press `Ctrl+C` to stop the program.

---


If `run.sh` is not executable: `chmod +x run.sh`.

Manual way (Linux / macOS):

```bash
mkdir -p build/classes
javac -cp lib/jade.jar -d build/classes src/main/java/dkai/jade/*.java
java -cp build/classes:lib/jade.jar dkai.jade.JadeDiningPhilosophers 1100 5
```

Manual way (Windows *maybe):

```bash
mkdir build\classes -Force
javac -cp lib\jade.jar -d build\classes (Get-ChildItem src\main\java\dkai\jade\*.java).FullName
java -cp "build\classes;lib\jade.jar" dkai.jade.JadeDiningPhilosophers 1100 5
```
