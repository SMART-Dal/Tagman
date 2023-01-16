_This README is intended for demo and internal development purposes only._
# Tagman	
Tagman is a web platform to create a manually annotated dataset of smells. The principle aim of Tagman is to facilitate users to annotate randomly selected code snippets for particular smells that could exist in the code and use the platform to create a sizeable quality dataset containing manually annotated samples for subjective code smells. We have considered four code smells we believe are subjective in nature: Multifaceted Abstraction, Complex Method, Long Method and Empty Catch Block
## Table of Contents

* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
     * [Software](#software)
     * [Python Libraries](#python-libraries)
  * [Setup](#setup)
* [Usage](#usage)
 

## Getting Started
In order to run Tagman, the following dependencies are necessary.
### Prerequisites
#### Software
* Java version 8 or above
* Apache Maven
Tested on Ubuntu 20.04.4 LTS.
### Setup
To setup the Tagman server, follow the following steps

1) Clone the repository
``` console
git clone https://github.com/SMART-Dal/Tagman_Phase2.git
```
2) Run maven build
In the project directory, run the following command:
``` console
Maven clean install
```


## Usage
A GCR file is composed of two main sections: Tasks and Representations. The Tasks block contains the definition of one or multiple tasks, some logic follows for the Representations block.
```
Tasks {

}

Reprs {

}

```
### Tasks
A task, is a set of operations that are executed on a base graph representation of code (base representation is defined in the [Representations](#representations) section). Optionally, they can be coupled with a set of conditions.  
We define an Operation to be an action executed on the elements of the graph (i.e Vertices and Edges). Currently, GCR supports edge addition and node removal.  
* Edge Addition: In this operation, specific edges are added between the verticies of the graph. The types of edges that are added aim to capture the semantics that the base representation fails at capturing. For instance, the Abstract Syntax Tree (AST) does not represent the control flow nautre of code, hence, adding edges between predicate nodes and other nodes (e.g between `while_condition` nodes and `while_body` nodes) could help with such short coming. To see the full list of supported edges, please refer to the appendix.

* Node Removal: Usually, a node represents a statement or a part of it. The rationale behind designing such operation, is to reduce the size of the graph. This is (along with edge addition) a design choice that depends on machine learning task. For instance when trying to classify code smells, we may not need print or logging statements, hence removing those could reduce the output size.  
To see the full list of node removal strategies, please refer to the appendix.  

The conditions within tasks can be divided into two categories:  
* Code Conditions: A code condition states what parts of code that should not be mutated (i.e an operation should not be applied of that part). This is done using the `exclude` keyword. Again this depends on the nature of task that we would like to achieve. Continuing with code smell detection: we may not want to remove print or logging statements blindly, because we might introduce code smells such as [Empty Catch Block](https://tusharma.in/smells/IECB.html).

* Structure Conditions: These conditions relate to structure of the graph like the maximum number of verticies and edges that graph should not exceed (i.e if it does, it should be deleted).  

The snippet below describes a task named `task1`, where `COMPUTED_FROM`, `LAST_LEXICAL_USE`, `WHILE_CFG` and `NEXT_TOKEN` edges should be added, print statement should be removed, catch and while blocks should remain intact and if a graph has lower than 4 verticies or higher than 500, should be discarded.

```
    task1 {
        Edge add next_token
        Edge add computed_from
        Edge add last_lexical_use
        Edge add while_cfg
        Node remove print
        conditions {
            exclude catch_block
            exclude while_block
            Node count [4,500]
        }
    }

```

### Representations
A repsentation is made of three components: a CSV file that contains the source code repositories, base graphs, and the set of tasks to be applied.
  * CSV File: This is just a list of directories, where each directory is a repository of a source code.
  * Base Graphs: These are the graphs that will be augmented using the defined tasks. Currently, GCR supports the following: Abstract Syntax Tree, Control Flow Graph and Program Dependence Graph. If there are multiple base graphs, they will be merged into one structure.
  * Tasks: one or a set of tasks.

The following example shows that two reprensentations, named `r1` and `r2` , should be contructed, where `r1` uses the AST as a base graph and `task1`, `task3` shall be executed on that AST, and `r2` uses a combination of AST and PDG and a pipeline of `task2`, `task4` and `task6`.  
```
    r1 {
        "/home/user/repos_group1.csv"
        AST
        task1
        task3
    }

    r2 {
        "/home/user/repos_group2.csv"
        AST
        PDG
        task2
        task4
        task6
    }
```  
_Check the `impl/examples` folder for full examples._  

To run a `.gcr` file run the following command:
``` console
python impl/run.py <GCR-FILE-NAME>.gcr
```
During executioon (which may take a while depending on the machine) a folder called `workspace` is created. This is normal behaviour, it is created by joern. Once the GCR file is finished executing it will be automatically removed.  
The results of each representation are located in folder called `gcr_out` in directory of the repository. For instance if there are two reprensetations called `r1` and `r2` and a directory `~/path/MyProject` located in the CSV files of `r1` and `r2`, then `MyProject` will have `gcr_out` with two subfolders `r1` and `r2` that contain the representation of each method.
```
├── MyProject
│  ├── <files-and-folders>
│  │   
│  ├── gcr_out
│  │   
│  │   ├── r1
│  │   │    ├── <METHOD_ID>_<METHOD_NAME>.dot
│  │   │    ...
│  │   │    └──  <METHOD_ID>_<METHOD_NAME>.dot
│  │   └── r2
│  │   │    ├── <METHOD_ID>_<METHOD_NAME>.dot
│  │   │    ...
│  │   │    └──  <METHOD_ID>_<METHOD_NAME>.dot
```

## Roadmap
  * Produce the initial embedding of each graph using word2vec.

## Appendix
### Types of Supported Edge Addition
This list of edges is taken from the literature in the area of source code analysis using machine learning.
| Edge                | Description                                                                                                                               | Base Graph | Original Paper |
|---------------------|-------------------------------------------------------------------------------------------------------------------------------------------|------------|----------------|
| AST*                | Edge between AST nodes                                                                                                                    | AST        |                |
| NEXT_TOKEN          | Edge between the leaf nodes of the AST of capture the sequential nature of code.                                                          | AST        | [^1]           |
| LAST_WRITE          | Connects a variable to the set of syntax tokens at which the variable could have been used last.                                          | AST        | [^1]           |
| LAST_READ           | Connects a variable to the set of syntax tokens  at which the variable was last written to.                                               | AST        | [^1]           |
| COMPUTED_FROM       | When there is a statement such as v = expr, edges are connected  between v and all variables in expr.                                     | AST        | [^1]           |
| LAST_LEXICAL_USE    | Links the occurrences of a variable v independently of the data flow.                                                                     | AST        | [^1]           |
| RETURNS_TO          | Links return tokens to the method's declaration                                                                                           | AST        | [^1]           |
| GUARDED_BY          | Connects every token corresponding to a variable  to enclosing guard expressions that use the variable when the guard condition is true.  | AST        | [^1]           |
| GUARDED_BY_NEGATION | Connects every token corresponding to a variable  to enclosing guard expressions that use the variable when the guard condition is false. | AST        | [^1]           |
| CFG*                | Edges between CFG nodes.                                                                                                                  | CFG        |                |
| PDG*                | Edges between PDG nodes.                                                                                                                  | PDG        |                |
| WHILE_CFG           | Creates two edges: WHILE_EXEC edge from the while condition to the while body and WHILE_NEXT in the opposite direction.                   | AST        | [^2]           |
| FOR_CFG             | Creates two edges: FOR_EXEC edge from the for-loop control nodes to the for-loop body and FOR_NEXT in the opposite direction.             | AST        | [^2]           |

*: Generated automatically by joern when the base graph is specified in the representation specification.

### Supported Node Removal

| Node              | Description                                                          |
|-------------------|----------------------------------------------------------------------|
| Print             | Remove print statements.                                             |
| Logging           | Remove logging statements.                                           |
| Sys Exit          | Remove exit statements (e.g System.exit() in java or exit() in C++). |
| Simple Assignment | Assignment statements in which the RHS does not contain variables.   |

### Supported Code Condition
* Action Keywords:
  - `exclude`: States that the code part should not be altered.
  - `include`: States that the code part can be altered.
* Code blocks;
  - Catch blocks.
  - For-loop blocks.
  - If blocks.
  - While blocks.

### Supported Structure Condition
* Structural Elements:
  - Node.
  - Edge.

* Structural Properties:
  - `count`: Boundaries on the number of the structural elements within the graph. E.g. what is min/max number of nodes a graph should have.

[^1]: [Learning to Represent Programs with Graphs](https://arxiv.org/abs/1711.00740)  
[^2]: [Detecting Code Clones with Graph Neural Network and Flow-Augmented Abstract Syntax Tree](https://arxiv.org/abs/2002.08653)
