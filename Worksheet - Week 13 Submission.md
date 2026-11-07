# COMP2000 Worksheet 2 — Final Submission

**Student name:** Toufique Rahman Tahsin

**GitHub repo URL:** https://github.com/ToufiqueRahmanTahsin/comp2000-simulation-assignment-fork

---

## 1. Design Patterns

Don't forget to submit a pdf file which shows a diagram of your pattern implemenation to supplement this text description.

**1.1.** Which design pattern(s) did you implement? Name them and state where each appears in your codebase.

 We implemented the Strategy design pattern to manage different ant movement behaviours. They appear in:

 MoveStrategy.java,
 Movement.java,
 Ant.java,
 Scout.java,
 Forager.java and
 Soldier.java.


**1.2.** For each pattern, paste a code snippet showing the key interaction between participants.

 **Snippets for Strategy design pattern:**

 **MoveStrategy.java:**

 @FunctionalInterface
 public interface MoveStrategy {
    void chooseAndMove(Ant ant, World world);
 }

 **Ant.java:**

 protected MoveStrategy strategy;

 Inside update(), when the ant is not returning home:

 strategy.chooseAndMove(this, world);

 **Scout.java:**

 super(x, y, 7.0, world, Movement::random);

 **Forager.java:**

 super(x, y, 4.0, world, Movement::followFoodTrail);


**1.3.** For each pattern, explain why you chose it over an alternative. What trade-off did you make?

 We chose Strategy pattern instead of placing all movement logic inside Ant.java using conditional statements for each ant type.
 
 The strategy pattern separates movement algorithms from the ant classes, making behaviours easier to reuse and modify.
 
 An alternative could be creating a separate concrete strategy class for every movement behaviour. But this would introduce additional classes and increase the complexity.
 
 We used a functional interface with method references to achieve the flexibility of Strategy while keeping the implementation concise.
 
 The trade-off is that movement logic is distributed across different files, which can require additional navigation when debugging. We considered this worth because it improves separation of responsibilities and also improves maintainability.


---

## 2. Streams / Lambdas / Parallelism

**2.1.** List every file in your project that uses lambda expressions or the Streams API.

 The following files use lambda expressions or the Streams API:

 Movement.java,
 Colony.java,
 World.java,
 SimulationPanel.java,
 SimulationFrame.java and 
 Main.java.

**2.2.** For the most sophisticated use, paste the code and explain: what does it do, and why was a stream/lambda better than a loop?

 **Colony.java:**
 public Map<String, Long> countByRole() {
    return ants.stream().collect(
        Collectors.groupingBy(
            Ant::roleName,
            Collectors.counting()
         )
      );
   }

 This method processes the colony's ants, groups them according to their roles and counts the number of ants in each group.
 
 The result is a map containing the populations of Scouts, Foragers and Soldiers. These statistics are displayed in SimulationPanel.java.
 
 A traditional loop would require manually checking each ant's role and updating counters or map entries.
 
 Using groupingBy() with counting() expresses the grouping and aggregation directly. This reduces repetitive code and makes the operation easier to understand and maintain.


**2.3.** Did you use parallel streams, custom collectors, or the Fork/Join framework? If yes, how did you ensure thread safety? If no, where could parallelism be introduced?

 Yes. We implemented parallel streams for read-only calculations.

 **Colony.java**

 public long countCarryingFoodParallel() {
     return ants.parallelStream()
         .filter(Ant::isCarryingFood)
         .count();
 }

 **Movement.java:**

 public static double averagePheromoneStrength(
         List<Pheromone> pheromones) {
     return pheromones.parallelStream()
         .filter(Pheromone::isActive)
         .mapToDouble(Pheromone::getStrength)
         .average()
         .orElse(0.0);
 }

 Both methods perform calculations without modifying the objects being processed.

 We kept ant movement, food collection and pheromone updates sequential. The simulation updates through a Swing timer, so these operations are not performed concurrently with the parallel calculations in our current design.

 We also tested the parallel ant count against its sequential equivalent and observed matching results.

 We did not implement a custom collector or manually manage Fork/Join tasks because Java's built-in stream operations were sufficient.

 For a relatively small colony, parallel processing may introduce more overhead than performance benefits. So we prioritised correctness and thread safety rather than parallelising the entire simulation.


---

## 3. Uniqueness and Creativity

**3.1.** What did you add to your simulation that went beyond the in-class activities? Describe the feature and what inspired it.

 We extended our week 7 implementation by introducing a third ant role, the Soldier.

 Soldiers move around the environment independently and deposit weaker pheromone trails. We also implemented role-specific movement strategies and displayed live colony statistics, including the number of ants in each role, remaining food, and food collected in the nest.


**3.2.** Paste a code snippet or describe a design decision that demonstrates emergent behaviour, non-trivial complexity, or a genuinely surprising outcome.

 One of our more complex design decisions was implementing pheromone-guided foraging rather than directly assigning food locations to every ant.

 In World.java, the findFoodTrail() method selects suitable pheromone trails based on several conditions. These include whether the pheromone is active, whether its food source is available, its distance from the nest and its strength.

 Scouts explore the environment and discover food sources. When ants return with food, they leave pheromone trails that Foragers can follow. These trails gradually decay,and trails linked to depleted food sources become inactive. This allows coordinated foraging behaviour to emerge from individual movement rules and interactions with the environment rather than requiring a central controller to direct every ant.



**3.3.** If you had another 6 weeks, what would you add or change?

 We would introduce obstacles or make movement more realistic. We would also improve the Soldier role by giving Soldiers a more distinct responsibility. Such as: protecting the nest from environmental threats.

 Additionally, we would experiment with larger colonies and investigate whether parallel processing improves performance as the number of ants increases.

 These improvements would add complexity and realism while building on our existing project.


---

## 4. Log Book and Final Reflection

**4.1.** Attach or link your log book entries for Weeks 7–13.

 It is attached with the submission.



**4.2.** Reflect on the semester:

- What was the most important concept you learned?

 The most important concept we learned was how to separate responsibilities using design principles and design patterns.

 Implementing the Strategy pattern helped us understand how behaviour can be separated from individual classes.

- What design trade-off did you face, and how did you resolve it?

 One important trade-off was choosing between a flexible movement system and keeping the code simple.

 We could have created separate classes for every movement strategy. But this would have increased the number of classes and made the project more complicated. Instead, we combined the Strategy pattern with a functional interface and method references. This allowed us to reuse movement algorithms without introducing unnecessary classes.

- If you started over, what would you do differently?

 We would plan the class architecture and movement system earlier.

 Having a clearer understanding of how Scouts, Foragers, Soldiers and pheromone trails interact would have helped us organise the code more effectively from the beginning.

- What feedback from the Week 7 submission did you act on?

 We did not have variety of behaviour in our week 7 submission. We added the Soldier ant role alongside Scouts and Foragers, giving it independent movement and the ability to leave weak pheromone trails. This also helped us demonstrate inheritance and reuse our existing movement strategies without adding unnecessary complexity.