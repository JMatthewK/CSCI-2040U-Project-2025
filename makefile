# Compiler
JAVAC = javac
JAVA = java
JAR = jar

# Directories
SRC_DIR = src/main/java
OUT_DIR = out
LIB_DIR = lib

# Classpath
CLASSPATH = $(LIB_DIR)/*

# Find all Java files
SOURCES = $(wildcard $(SRC_DIR)/*.java)
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(OUT_DIR)/%.class)

# Main class to run
MAIN_CLASS = CatalogApp

# Build the project
all: $(CLASSES)

# Compile .java files only if they are newer than .class files
$(OUT_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(OUT_DIR)
	$(JAVAC) -d $(OUT_DIR) -cp "$(CLASSPATH)" $<

# Force rebuild if needed
.PHONY: all clean run

# Run the project
run: all
	$(JAVA) -cp "$(OUT_DIR);$(CLASSPATH)" $(MAIN_CLASS)

# Clean compiled files
clean:
	rm -rf $(OUT_DIR)/*.class
#TO RUN PROJECT ENTER THE FOLLOWING COMMANDS
#make clean
#make
#make run
