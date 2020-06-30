# Digital Input with Pin-Change Interrupts

Input digital pins are readable as `high` or `low`.

Most pins generate pin-change interrupts in blocks.

A block is a set of pins associated with the same interrupt:
- `PCINT0` is a block of pins in the range `PCINT[0-7]`,
- `PCINT1` is a block of pins in the range `PCINT[8-15]`,
- and so on.

*NOTE: The name "PCINT" is used for both the block interrupt number and the
individual pins (e.g., the pin `PCINT1` is in the range of the interrupt
`PCINT0`.)*

Pin mapping in popular Arduinos:

```
        pcint0      pcint1
UNO     D8-D12       A0-A5
MEGA   D53-D50       D0
       D10-D13     D15,D14
```

- In the `UNO`, the pin `PCINT2` is associated with interrupt `PCINT0` and
  corresponds to pin `D10`.
- In the `MEGA`, the pin `PCINT8` is associated with interrupt `PCINT1` and
  corresponds to pin `D0`.

The `X` in all occurrences of `PCINTX` or `pcintX` is a placeholder for a digit
that refers to a block of pins.

## API

### Includes

```
#include "pcintX.ceu"
```

### Inputs

#### PCINTX

```
input none PCINTX;
```

- Occurrences:
    - whenever the state of the any of the pins in the block changes
- Payload:
    - `none`

### Code Abstractions

#### PCINTX_Enable

Enables or disables pin-change interrupts for the provided pin in the block.

```
code/call PCINTX_Enable (var int pcint, var on/off v) -> none;
```

Parameters:

- `int`:    individual pin to configure
- `on/off`: enable or disable interrupts

Return:

- `none`

#### PCINTX_Get

Gets the current state of the provided pin in the block.

```
code/call PCINTX_Get (var int pcint) -> high/low do
```

Parameters:

- `int`: individual pin to query (e.g., `_PCINT4`)

Return:

- `high/low`: current state of the pin

#### PCINTX_Demux

Demultiplexes pin-change block interrupts into specific pin events.

```
code/await PCINTX_Demux (none) -> (event (int,high/low) e) -> NEVER;
```

Parameters:

- `none`

Public fields:

- `event (int,high/low)`: triggered on block interrupts carrying the specific changed pin and its new state

Return:

- `NEVER`: never returns

*NOTE: Pins must be [enabled](#pcintx_enable) individually.*

## Examples

### Input / Output

State of output pin 13 follows the state of pin associated with *PCINT4*:

```
#include "out.ceu"
#include "pcint0.ceu"

output high/low OUT_13;

call PCINT0_Enable(_PCINT4, on);   // UNO=D12, MEGA=D10

emit OUT(13, call PCINT0_Get(_PCINT4));

loop do
    await PCINT0;
    emit OUT(13, call PCINT0_Get(_PCINT4));
end
```

### Enable / Disable

Similar to previous example, but switches interrupts on and off every `4`
seconds:

```
#include "out.ceu"
#include "wclock.ceu"
#include "pcint0.ceu"

output high/low OUT_13;

spawn do
    loop do
        call PCINT0_Enable(_PCINT4, on);   // UNO=D12, MEGA=D10
        await 4s;
        call PCINT0_Enable(_PCINT4, off);  // UNO=D12, MEGA=D10
        await 4s;
    end
end

emit OUT(13, call PCINT0_Get(_PCINT4));

loop do
    await PCINT0;
    emit OUT(13, call PCINT0_Get(_PCINT4));
end
```

### Demux

Similar to the first example but uses demux to handle events:

```
#include "out.ceu"
#include "pcint1.ceu"

output high/low OUT_13;

call PCINT1_Enable(_PCINT8, on);   // UNO=A0, MEGA=D0

var& PCINT1_Demux pcint1 = spawn PCINT1_Demux();

emit OUT(13, call PCINT1_Get(_PCINT8));

loop do
    var int pin;
    var high/low v;
    (pin,v) = await pcint1.e until (pin == _PCINT8);
    emit OUT(13, v);
end
```
