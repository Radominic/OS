#ifdef _MSC_VER
#define _CRT_SECURE_NO_WARNINGS
#endif

#include <stdio.h>
#include <stdlib.h> 
#include <ctype.h>
struct Node *printNode(struct Node*);
struct Node *inputPoly(void);
struct Node *multiply(struct Node*, struct Node*);

main(void) {

	struct Node* poly1 = inputPoly();
	printNode(poly1);

	struct Node* poly2 = inputPoly();
	printNode(poly2);

	printNode(multiply(poly1, poly2));

}

struct Node {
	int degree;
	int coefficient;
	struct Node *next; //link to the next node. Null when the last node.
	struct Node *prev; //link to the previous node. Null when the fist node.
};

//function for input polinomial
struct Node *inputPoly(void) {
	//init one node and fields.
	struct Node *poly = (struct Node*)malloc(sizeof(struct Node));
	poly->next = NULL;
	poly->prev = NULL;
	poly->degree = NULL;
	poly->coefficient = NULL;
	//this 'while' repeat the input
	while (1) {
		int degree;
		int coefficient;


	//this is exception for error of bogus input such as double or char.
		while (1) {
			printf("Input(degree)(coefficient):");
			if (scanf("%d %d", &degree, &coefficient) != 2) {
				printf("error : input only integer!\n");
				while (getchar() != '\n');
			}
			else {
				break;
			}
		}

		// if both two inputs are negative, function will be return polinomial with first node.
		if (degree < 0 && coefficient < 0) {
			printf("Done!!\n");
			return poly;
		}// case for only one of inputs is negative.
		else if (degree < 0 || coefficient < 0 || coefficient == 0) {
			continue;
		}
		else {
			//if two inputs are positive, have to check this is first node or not.
			if (poly->coefficient == NULL&& poly->degree == NULL) {
				poly->degree = degree;
				poly->coefficient = coefficient;
			}
			else {
				//if this is not first, make temporal node using malloc().
				struct Node *current = (struct Node*)malloc(sizeof(struct Node));
				struct Node *temp, *pointer;
				current->degree = degree;
				current->coefficient = coefficient;
				pointer = poly;
				//we have to sort the result polinomial with ascending. so using the characteristic of linked list, can find the correct location of node.
				//when exchange the location of node, use the temp and pointer.
				//when input dgree is bigger than current polinomial's pointer
				if (pointer->degree < degree) {

					while (1) {
						if (pointer->next == NULL) {
							pointer->next = current;
							current->prev = pointer;
							current->next = NULL;
							break;
						}
						else if (pointer->next->degree > degree) {
							temp = pointer->next;
							pointer->next = current;
							current->prev = pointer;
							current->next = temp;
							temp->prev = current;
							break;
						}
						else {
							//this is exception case of input same dgree
							if (pointer->next->degree == degree) {
								printf("error : you use same degree!\n"); break;
							}
							//exchange pointer to high degree.
							pointer = pointer->next;
						}
					}
				}
				//when input dgree is smaller than current polinomial's pointer
				else if (pointer->degree > degree) {
					while (1) {
						if (pointer->prev == NULL) {
							current->next = pointer;
							pointer->prev = current;
							current->prev = NULL;
							poly = current;
							break;
						}
						else if (pointer->prev->degree < degree) {
							temp = pointer->prev;
							pointer->prev = current;
							current->next = pointer;
							current->prev = temp;
							temp->next = current;
							poly = temp;
							break;
						}
						else {
							pointer = pointer->prev;
						}
					}
				}
				else {
					printf("error : you use same degree!\n");
				}
			}
		}
	}
}
//this function for print node using parpameter Node.
struct Node *printNode(struct Node* poly) {
	//exception for Null error when polinomial is Null.
	if ((poly->degree == NULL&&poly->coefficient == NULL)||poly->coefficient ==NULL) {
		printf("error : there is not list!\n");
	}
	else {
		while (1) {
			if (poly->degree == 0) {
				printf("%d",poly->coefficient);
			}
			else if (poly->degree  > 1 ) {
				printf("%d X^%d", poly->coefficient, poly->degree);
			}
			else {
				printf("%d X", poly->coefficient);
			}
			if (poly->next == NULL) {
				break;
			}
			printf(" + ");

			poly = poly->next;
		}
	}
	printf("\n");
}
//multiply two polinomial using two node parameter.
struct Node *multiply(struct Node* a, struct Node* b) {
	//btemp is temporal node for polinomial b.
	//result is result polinomial and rptr is pointer for result polinomial.
	struct Node *btemp, *result, *rptr;
	result = (struct Node*)malloc(sizeof(struct Node));
	result->degree = NULL;
	result->next = NULL;
	result->prev = NULL;
	result->coefficient = NULL;
	rptr = result;
	//exection for case of can't multiply
	if ((a->degree == NULL && a->coefficient == NULL)||( b->degree == NULL && b->coefficient == NULL)) {
		printf("error : can't multiply!\n");
		return result;
	}
	//bellow source code is simmilar with input function. this is also for sort.
	//i can separate this code with name 'add'function for efficiency but question reauilre only input and multiply function.
	while (1) {
		// this move from fist a and b to last a and b.
		btemp = b;
		while (1) {
			//temp is temporal node for sort.
			struct Node *temp = (struct Node*)malloc(sizeof(struct Node));
			temp->next = NULL;
			temp->prev = NULL;
			//this is when input is  a constant.
			if (a->degree == 0) {
				temp->degree = btemp->degree;
			}
			else if(btemp->degree == 0) {
				temp->degree = a->degree;
			}
			else {
				temp->degree = a->degree + btemp->degree;
			}
			temp->coefficient = a->coefficient * btemp->coefficient;
			//make the result polinomial using rptr
			if (rptr->degree == NULL) {
				rptr = temp;
			}
			else {
				//compare rptr'degree and current degree of multiply of a and b
				//when current degree is bigger
				if (temp->degree > rptr->degree) {

					while (1) {
						if (rptr->next == NULL) {

							rptr->next = temp;
							temp->prev = rptr;
							temp->next = NULL;
							break;
						}
						else if (rptr->next->degree > temp->degree) {
							temp->prev = rptr;
							temp->next = rptr->next;
							rptr->next = temp;
							temp->next->prev = temp;
							break;
						}
						else {
							//when degrees are same, just add two coefficients.
							if (rptr->next->degree == temp->degree) {
								rptr->next->coefficient += temp->coefficient;
								break;
							}
							rptr = rptr->next;
						}
					}
				}
				//when current degree is smaller
				else if (temp->degree < rptr->degree) {
					while (1) {
						if (rptr->prev == NULL) {
							rptr->prev = temp;
							temp->next = rptr;
							temp->prev = NULL;
							result = temp;
							break;
						}
						else if (rptr->prev->degree < temp->degree) {
							temp->prev = rptr->prev;
							temp->next = rptr;
							rptr->prev->next = temp;
							temp->next->prev = temp;
							rptr = temp->prev;
							break;
						}
						else {
							if (rptr->prev->degree == temp->degree) {
								rptr->prev->coefficient += temp->coefficient;
								break;
							}
							rptr = rptr->prev;
						}
					}
				}
				else {
					rptr->coefficient += temp->coefficient;
				}

			}
			//move btemp to next btemp;
			if (btemp->next == NULL) { break; }
			btemp = btemp->next;
		}
		//move a to next a;
		if (a->next == NULL) { break; }
		a = a->next;
	}
	//move rptr to first node for print result of multiply
	while (1) {
		if (rptr->prev == NULL) {
			break;
		}
		rptr = rptr->prev;
	}
	result = rptr;
	printf("multiply result : ");
	return result;
}



