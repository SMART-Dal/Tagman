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

