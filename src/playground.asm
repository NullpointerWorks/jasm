.def EXIT 0

jmp main

; ===========================
; multiply method
; ===========================
multiply:
	push c
	load c,0
	inc a
_mul_loop:
	dec a
	je _mul_done
	add c,b
	jmp _mul_loop
_mul_done:
	load a,c
	pop c
	ret
	
; ===========================
; program main
; ===========================
main:
	load a,7
	load b,7
	call multiply
	int 10
	int EXIT
	