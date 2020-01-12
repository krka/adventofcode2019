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
  if opcode == 1 then
     program[program[i + 3]] = program[program[i + 1]] + program[program[i + 2]]
  else
    if opcode == 2 then
      program[program[i + 3]] = program[program[i + 1]] * program[program[i + 2]]
    else
      if opcode == 99 then
        return program[0]
      else
        throw()
      end
    endif
  endif
  i = i + 4
  jump loop
endfunc

func find()
  int i, j = 0, 0
loop:
  if compute(i, j) == 19690720 then
    return 100*i + j
  endif
  j = j + 1
  if j < 100 jump loop
  j = 0
  i = i + 1
  if i < 100 jump loop
  throw()
finish:
endfunc

int ans = find()
output(ans)

