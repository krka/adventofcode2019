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
