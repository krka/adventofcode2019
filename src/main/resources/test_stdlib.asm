#include stdlib.asm

assert(1)
assert(2)

string hell = "hell"
string hello = "hello"
string world = "world"
string hello2 = "hello"

assert(strlen(hello) == 5)

assert(strcmp(hello, world) < 0)

assert(strcmp(hello, hello2) == 0)

assert(strcmp(hello, hell) > 0)
