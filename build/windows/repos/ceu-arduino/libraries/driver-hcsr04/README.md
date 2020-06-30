# HC-SR04 - Ultrasonic Sensor

Ultrasonic ranging sensor:

https://www.sparkfun.com/products/13959

## API

### Macros

These macros must be defined before including the driver:

```
#define HCSR04_ECHO     <fill-with-input-pin>
#define HCSR04_TRIGGER  <fill-with-output-pin>
```

*NOTE: The current driver only supports a single device.*

### Includes

```
#include "hcsr04.ceu"
```

### Code Abstractions

#### Hcscr04

Measures the distance from an obstacle:

```
code/await HCSR04_Init (none) -> int?;
```

Parameters:

- `none`

Return:

- `int?`: the distance found (if any)

The measures takes at most `25ms`.

The recommended period between measures is `60ms`.

## Examples

### Periodic measures

Measures the distance continuously with a period of at least `60ms`:

```
#include "out.ceu"
#include "int0.ceu"
#include "wclock.ceu"

output high/low OUT_07;
output high/low OUT_13;

#define HCSR04_ECHO    INT0
#define HCSR04_TRIGGER OUT_07
#include "hcsr04.ceu"

loop do
    var int? d = await HCSR04_Init();
    emit OUT_13(d? and ((d! <= 30) as high/low));
    await 60ms;     // minimum recommended time between measures
end
```
