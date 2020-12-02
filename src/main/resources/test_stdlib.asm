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

int x = 3
int a, b, c, d = 1, 2, x, 4
assert(a == 1)
assert(b == 2)
assert(c == 3)
assert(d == 4)

a = ' '
b = 32
assert(a == b)

int value = read_int()
output(value)
assert(value == 1234)

array[10] str
skip_non_chars()
read_chars(str)
assert(strcmp(hello, str) == 0)
