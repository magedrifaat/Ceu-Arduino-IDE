# PWM Output

`PWM` output pins are writable with values between `0` and `255`.

## API

### Includes

```
#include "pwm.ceu"
```

### Outputs

#### PWM

Sets the value of a parameterized PWM pin.

```
output (int, u8) PWM;
```

Parameters:

1. `int`: pin to change
2. `u8`:  new value of the pin

*NOTE: Does not work with the power management enabled.*

### Code Abstractions

#### Pwm

Sets the value of a parameterized PWM pin.

```
code/await Pwm (int, u8) -> NEVER;
```

Parameters:

1. `int`: pin to change
2. `u8`:  new value of the pin

Return:

- `NEVER`: never returns

## Examples

### Output event

Writes `128` (half of the reference voltage) to pin 6:

```
#include "pwm.ceu"

emit PWM(6, 128);

await FOREVER;
```

*NOTE: Compile with `CEU_DEFS=-DCEU_PM_MIN` to disable the power manager. (Use [Pwm](#pwm-1) instead.)*

### Code abstraction

Writes `128` (half of the reference voltage) to pin 6:

```
#include "pwm.ceu"

await Pwm(6, 128);
```

### Dimming an LED back and forth

Fades pin 6 slowly from `0` to `255` and back to `0` continuously:

```
#include "pwm.ceu"
#include "wclock.ceu"

loop do
    var int i;
    loop i in [0->255] do
        spawn Pwm(6, i);
        await 5ms;
    end
    loop i in [0<-255] do
        spawn Pwm(6, i);
        await 5ms;
    end
end
```
