using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class Card
    {
        private static int nextID = 0;

        public int id;
        public int order;
        public int owner;   // -1: on stack there is no owner

        public Card()
        {

        }

        public Card(int order, int owner)
        {
            this.id = nextID++;
            this.order = order;
            this.owner = owner;
        }
    }
}