package com.fun.multiselectpopupwindows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fun.multiselectpopupwindows.model.Holedata_method;

import java.util.ArrayList;
import java.util.List;


public class PersonAdapter extends ArrayAdapter<Person>{

    private int resourceId;
    private Context context;
    private  List<Person> objects;
    public static List<Integer> index_o;
//    public PersonAdapter adapter;

    public PersonAdapter(Context context, int textViewResourceId, List<Person> objects){
        super(context, textViewResourceId, objects);
        this.objects = objects;//
        resourceId = textViewResourceId;
        this.context = context;
        index_o = new ArrayList<>();
    }

    public View getView(final int position, View convertView, final ViewGroup parent){
        final Person person = getItem(position);
        final View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.delete = view.findViewById(R.id.delete_button);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
//        viewHolder.image.setImageResource(person.getImageId());
        viewHolder.name.setText(person.getName());

        viewHolder.delete = view.findViewById(R.id.delete_button);
        viewHolder.delete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                for (int i = 0; i < objects.size(); i++)
//                {
//                    System.out.println(objects.get(i).getName());
//                }
//                System.out.println(objects);
                System.out.print("**************************");
                System.out.print(position);

                System.out.print("**************************");
                index_o.add(position);
                objects.remove(position);

//                System.out.println(objects);
                HoleActivity.adapter.notifyDataSetChanged();
//                MainActivity.holedatas_1=objects;
//                MainActivity.holedatas_1 = new ArrayList<>();
                MainActivity.holedatas_1.clear();//初始化清空
                for (int i = 0; i < objects.size(); i++)
                {

                    MainActivity.holedatas_1.add(new Holedata_method(objects.get(i).getName()));
                    System.out.println(objects.get(i).getName());
                }
//                 PersonAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(), "you clicked delete button", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    class ViewHolder{
        TextView name;
        View delete;
    }
}
