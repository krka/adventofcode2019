#include stdlib.asm

int N = input()
array[N] source

int i = 0
while i < N
  source[i] = input()
  i = i + 1
end

func compute(a, b)
  array[N] program
  memcpy(N, source, program)

  program[1] = a
  program[2] = b

  int i = 0
  while 1
    int opcode = program[i]
    if opcode == 1 then
      program[program[i + 3]] = program[program[i + 1]] + program[program[i + 2]]
    elseif opcode == 2 then
      program[program[i + 3]] = program[program[i + 1]] * program[program[i + 2]]
    elseif opcode == 99 then
      return program[0]
    else
      throw()
    end
    i = i + 4
  end
  # unreachable
  throw()
end

output(compute(12, 2))
