#include stdlib.asm

int N = input()
array[N] source

for i = 0, i < N do
  source[i] = input()
end

func compute(a, b)
  array[N] program
  memcpy(N, source, program)

  program[1] = a
  program[2] = b

  for i = 0, 1, 4 do
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
  end
  # unreachable
  throw()
end

output(compute(12, 2))
