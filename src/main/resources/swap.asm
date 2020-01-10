#include stdlib.asm

func swap(a, b)
  return b, a
endfunc

int x = 0
int y = 0
int z = 0
int w = 0

x = 100
y = 23

z, w = swap(x, y)

assert(z == 23)
assert(w == 100)

int a, b, c = 1, swap(3, 2)
assert(a == 1)
assert(b == 2)
assert(c == 3)

int a2, b2, c2 = swap(3, 2), 3, 4
assert(a2 == 2)
assert(b2 == 3)
assert(c2 == 4)
