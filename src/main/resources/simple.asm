#include stdlib.asm

int a = 10 + 20
assert(a == 30)

int b = !0 + 2
assert(b == 3)

int c = a + b
assert(c == 33)

c = c + 90 + !1
assert(c == 123)

