int tmp = 0

func swap a b
return b a
endfunc

int x = 0
int y = 0
int z = 0
int w = 0

x = 100 + 0
y = 23 + 0

call swap x y : z w
output z
output w

halt
