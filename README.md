# rc2.lab.material
Repository with lab materials for the Computer Networks II course.

A `Makefile` is provided to facilitate compilation and cleanup:

- **`make compile DIR=<directory>`** → Compiles all `.java` files in the specified directory.
- **`make clean DIR=<directory>`** → Removes all `.class` files in the specified directory.
- **`make compileall`** → Compiles all `.java` files in the repository.
- **`make cleanall`** → Removes all `.class` files in the repository.
- **`make`** → Default target; runs `make compileall`.
