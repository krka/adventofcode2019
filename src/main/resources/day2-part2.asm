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

func find()
  int i, j = 0, 0
  while i < 100
    j = 0
    while j < 100
      if compute(i, j) == 19690720 then
        return 100*i + j
      end
      j = j + 1
    end
    i = i + 1
  end
  throw()
end

output(find())

