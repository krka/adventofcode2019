#include stdlib.asm

int N = input()
array[N] source
int i = 0
loop:
  source[i] = input()
  i = i + 1
if i < N jump loop

func compute(a, b)
  array[N] program
  memcpy(N, source, program)

  program[1] = a
  program[2] = b

  int i = 0
loop:
  int opcode = program[i]
  if opcode == 1 jump perform_add
  if opcode == 2 jump perform_mul
  if opcode == 99 jump finish
  throw()
perform_add:
  program[program[i + 3]] = program[program[i + 1]] + program[program[i + 2]]
  i = i + 4
  jump loop
perform_mul:
  program[program[i + 3]] = program[program[i + 1]] * program[program[i + 2]]
  i = i + 4
  jump loop
finish:
  return program[0]
endfunc

output(compute(12, 2))
