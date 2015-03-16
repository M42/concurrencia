main: main.cpp
	g++ $< -o $@ -lpthread -fpermissive
